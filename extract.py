import argparse

import cv2 as cv
import numpy as np
import matplotlib.pyplot as plt
import PIL
import os
import json

from sklearn.cluster import KMeans
from collections import Counter


def run(ann_path, img_path, lab_path, out_path, gamma=1.0):
    # ann_path = 'C:/Users/HeoSeokYong/yolo/yolov5/runs/detect/exp3/labels/(166)IMG_1.txt'
    # img_path = 'C:/Users/HeoSeokYong/yolo/yolov5/runs/detect/exp3/(166)IMG_1.jpg'
    # lab_path = 'C:/Users/HeoSeokYong/yolo/yolov5/colors_label.txt'
    # out_path = 'C:/Users/HeoSeokYong/yolo/yolov5/out.txt'
    result = ''

    color = get_color_label(lab_path)
    img = cv.imread(img_path)
    img = cv.cvtColor(img, cv.COLOR_BGR2RGB)
    g = gamma
    out = img.copy()
    out = out.astype(np.float)
    out = ((out / 255) ** (1 / g)) * 255
    out = out.astype(np.uint8)
    img = out

    h = img.shape[0]
    w = img.shape[1]

    f = open(ann_path, 'r', encoding='UTF8')
    o = open(out_path, 'w', encoding='UTF8')

    clt = KMeans(n_clusters=3)

    tmp = []
    while True:
        lines = f.readline()
        if not lines:
            break
        tmp.append(lines.strip('\n').split(' '))

    ann = [[0 for __ in range(5)] for _ in range(len(tmp))]
    for i in range(len(tmp)):
        ann[i] = [float(j) for j in tmp[i]][1:5]

    if tmp[0][0] == '4':  # vest를 sling으로
        tmp[0][0] = '5'

    if len(tmp) == 0 or len(tmp) > 2:
        result = '0, 0|0, 0'
    elif len(tmp) == 1:
        img1_ann = {'top': ann[0]}
        x1 = round((img1_ann['top'][0] - (img1_ann['top'][2] * 0.4)) * w)
        x2 = round((img1_ann['top'][0] + (img1_ann['top'][2] * 0.4)) * w)
        y1 = round((img1_ann['top'][1] - (img1_ann['top'][3] * 0.4)) * h)
        y2 = round((img1_ann['top'][1] + (img1_ann['top'][3] * 0.4)) * h)
        top = img[y1:y2, x1:x2].copy()
        clt_1 = clt.fit(top.reshape(-1, 3))
        if int(ann[0][0]) < 6:
            result = str(int(tmp[0][0]) + 1) + ', ' + str(
                color[int(what_color(palette_perc(clt_1, 1), color))][0]) + '|0, 0'
        elif int(ann[0][0]) > 8:
            result = '0, 0|0, 0'
        else:
            result = '0, 0|' + str(int(tmp[0][0]) - 5) + ', ' + str(
                color[int(what_color(palette_perc(clt_1, 1), color))][0])
    elif len(tmp) == 2:
        if int(tmp[0][0]) > int(tmp[1][0]):
            te = tmp[0]
            tmp[0] = tmp[1]
            tmp[1] = te
            tem = ann[0]
            ann[0] = ann[1]
            ann[1] = tem
        img1_ann = {'top': ann[0], 'bottom': ann[1]}
        x1 = round((img1_ann['top'][0] - (img1_ann['top'][2] * 0.4)) * w)
        x2 = round((img1_ann['top'][0] + (img1_ann['top'][2] * 0.4)) * w)
        y1 = round((img1_ann['top'][1] - (img1_ann['top'][3] * 0.4)) * h)
        y2 = round((img1_ann['top'][1] + (img1_ann['top'][3] * 0.4)) * h)
        x11 = round((img1_ann['bottom'][0] - (img1_ann['bottom'][2] * 0.4)) * w)
        x22 = round((img1_ann['bottom'][0] + (img1_ann['bottom'][2] * 0.4)) * w)
        y11 = round((img1_ann['bottom'][1] - (img1_ann['bottom'][3] * 0.4)) * h)
        y22 = round((img1_ann['bottom'][1] + (img1_ann['bottom'][3] * 0.4)) * h)

        top = img[y1:y2, x1:x2].copy()
        bot = img[y11:y22, x11:x22].copy()

        clt_1 = clt.fit(top.reshape(-1, 3))
        # print(color[int(what_color(palette_perc(clt_1, 1), color))][1])
        result += str(int(tmp[0][0]) + 1) + ', ' + str(color[int(what_color(palette_perc(clt_1, 1), color))][0]) + '|'

        clt_2 = clt.fit(bot.reshape(-1, 3))
        # print(color[int(what_color(palette_perc(clt_2, 1), color))][1])
        result += str(int(tmp[1][0]) - 5) + ', ' + str(color[int(what_color(palette_perc(clt_2, 1), color))][0])

    o.write(result)

    f.close()
    o.close()


def palette_perc(k_cluster, ver=0):
    width = 300
    palette = np.zeros((50, width, 3), np.uint8)

    n_pixels = len(k_cluster.labels_)
    counter = Counter(k_cluster.labels_)  # count how many pixels per cluster
    perc = {}
    for i in counter:
        perc[i] = np.round(counter[i] / n_pixels, 2)
    perc = dict(sorted(perc.items()))

    # for logging purposes
    step = 0
    for idx, centers in enumerate(k_cluster.cluster_centers_):
        palette[:, step:int(step + perc[idx] * width + 1), :] = centers
        step += int(perc[idx] * width + 1)
    if ver == 0:
        print(perc)
        print(k_cluster.cluster_centers_)
        return palette
    else:
        tmp = []
        for i in perc:
            tmp.append(perc[i])
        return k_cluster.cluster_centers_[tmp.index(max(tmp))]


def what_color(rgb, colors):
    idx = 0
    lat_sum = 200000
    gray_check = abs(np.mean(rgb) - rgb[0]) + abs(np.mean(rgb) - rgb[1]) + abs(np.mean(rgb) - rgb[2])
    if gray_check < 30 and 720 > sum(rgb) > 110:  # Gray
        return 8
    elif gray_check < 30:  # Black
        if sum(rgb) <= 110:
            return 0
        if sum(rgb) >= 720:  # White
            return 1
    for i in range(len(colors)):
        tsum = 0
        for j in range(3):
            tsum += (colors[i][2][j]-rgb[j])**2
        if tsum < lat_sum:
            if colors[i][0] == '3': # Gray 예외
                continue
            else:
                lat_sum = tsum
                idx = i
    return idx


def get_color_label(lab):
    label = open(lab, 'r', encoding='UTF8').read().split('\n')
    colors = []
    for la in label:
        colors.append(la.split(','))

    for c in colors:
        tmp = []
        tmp.append(int(c[2][0:2], 16))
        tmp.append(int(c[2][2:4], 16))
        tmp.append(int(c[2][4:6], 16))
        c[2] = tmp
    return colors


def parse_opt():
    parser = argparse.ArgumentParser()
    parser.add_argument('-a', '--ann_path', type=str,  required=True, help='path to annotation file')
    parser.add_argument('-i', '--img_path', type=str, required=True, help='path to image file')
    parser.add_argument('-l', '--lab_path', type=str, required=True, help='path to color label file')
    parser.add_argument('-o', '--out_path', type=str, required=True, help='path to output file')
    parser.add_argument('-g', '--gamma', type=float, default=1.0, required=True, help='gamma value')
    args = parser.parse_args()
    return args


def main(opt):
    run(**vars(opt))


if __name__ == "__main__":
    opt = parse_opt()
    main(opt)
