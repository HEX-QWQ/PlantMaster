# import json
# import cv2
# import Flask
# import numpy as np           
# import argparse, sys, os
# from GUIdriver import *
# import pandas as pd
# import requests

# app = Flask(__name__)

# def endprogram():
# 	print ("\nProgram terminated!")
# 	sys.exit()


# #Reading the image by parsing the argument 
# imgurl='https://tse1-mm.cn.bing.net/th/id/OIP-C.NtTAcr-kHZ6E5dVBkNq7FAHaE8?pid=ImgDet&rs=1'
# response = requests.get(imgurl, stream=True)

# # 检查响应状态码，确保请求成功
# if response.status_code == 200:
#     # 将图像数据转换为numpy数组
#     img_array = np.array(bytearray(response.content), dtype=np.uint8)
#     # 使用OpenCV解码图像数组
#     img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)
#     # 显示图像
# else:
#     print("Failed to download image")
# img = cv2.resize(img ,((int)(img.shape[1]/5),(int)(img.shape[0]/5)))
# original = img.copy()
# neworiginal = img.copy() 



# #Calculating number of pixels with shade of white(p) to check if exclusion of these pixels is required or not (if more than a fixed %) in order to differentiate the white background or white patches in image caused by flash, if present.
# p = 0 
# for i in range(img.shape[0]):
# 	for j in range(img.shape[1]):
# 		B = img[i][j][0]
# 		G = img[i][j][1]
# 		R = img[i][j][2]
# 		if (B > 110 and G > 110 and R > 110):
# 			p += 1
			
# #finding the % of pixels in shade of white
# totalpixels = img.shape[0]*img.shape[1]
# per_white = 100 * p/totalpixels
# '''
# print 'percantage of white: ' + str(per_white) + '\n'
# print 'total: ' + str(totalpixels) + '\n'
# print 'white: ' + str(p) + '\n'
# '''

# #excluding all the pixels with colour close to white if they are more than 10% in the image
# if per_white > 10:
# 	img[i][j] = [200,200,200]
	


# #Guassian blur
# blur1 = cv2.GaussianBlur(img,(3,3),1)


# #mean-shift algo
# newimg = np.zeros((img.shape[0], img.shape[1],3),np.uint8)
# criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER , 10 ,1.0)

# img = cv2.pyrMeanShiftFiltering(blur1, 20, 30, newimg, 0, criteria)
# #Guassian blur
# blur = cv2.GaussianBlur(img,(11,11),1)

# #Canny-edge detection
# canny = cv2.Canny(blur, 160, 290)

# canny = cv2.cvtColor(canny,cv2.COLOR_GRAY2BGR)
# #creating border around image to close any open curve cut by the image border 
# #bordered = cv2.copyMakeBorder(canny,10,10,10,10, cv2.BORDER_CONSTANT, (255,255,255))		#function not working(not making white coloured border) 
# #bordered = cv2.rectangle(canny,(-2,-2),(275,183),(255,255,255),3)
# #cv2.imshow('Canny on meanshift bordered image',bordered)


# #contour to find leafs
# bordered = cv2.cvtColor(canny,cv2.COLOR_BGR2GRAY)
# contours,hierarchy = cv2.findContours(bordered, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

# maxC = 0
# for x in range(len(contours)):													#if take max or one less than max then will not work in
# 	if len(contours[x]) > maxC:													# pictures with zoomed leaf images
# 		maxC = len(contours[x])
# 		maxid = x

# perimeter = cv2.arcLength(contours[maxid],True)
# #print perimeter
# Tarea = cv2.contourArea(contours[maxid])
# cv2.drawContours(neworiginal,contours[maxid],-1,(0,0,255))

# #cv2.imwrite('Contour complete leaf.jpg',neworiginal)



# #Creating rectangular roi around contour
# height, width, _ = canny.shape
# min_x, min_y = width, height
# max_x = max_y = 0
# frame = canny.copy()

# # computes the bounding box for the contour, and draws it on the frame,
# for contour, hier in zip(contours, hierarchy):
# 	(x,y,w,h) = cv2.boundingRect(contours[maxid])
# 	min_x, max_x = min(x, min_x), max(x+w, max_x)
# 	min_y, max_y = min(y, min_y), max(y+h, max_y)
# 	if w > 80 and h > 80:
# 		#cv2.rectangle(frame, (x,y), (x+w,y+h), (255, 0, 0), 2)   #we do not draw the rectangle as it interferes with contour later on
# 		roi = img[y:y+h , x:x+w]
# 		originalroi = original[y:y+h , x:x+w]
		
# if (max_x - min_x > 0 and max_y - min_y > 0):
# 	roi = img[min_y:max_y , min_x:max_x]	
# 	originalroi = original[min_y:max_y , min_x:max_x]
# 	#cv2.rectangle(frame, (min_x, min_y), (max_x, max_y), (255, 0, 0), 2)   #we do not draw the rectangle as it interferes with contour
# img = roi


# #Changing colour-space
# #imghsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
# imghls = cv2.cvtColor(roi, cv2.COLOR_BGR2HLS)

# imghls[np.where((imghls==[30,200,2]).all(axis=2))] = [0,200,0]


# #Only hue channel
# huehls = imghls[:,:,0]

# #ret, huehls = cv2.threshold(huehls,2,255,cv2.THRESH_BINARY)

# huehls[np.where(huehls==[0])] = [35]



# #Thresholding on hue image
# ret, thresh = cv2.threshold(huehls,28,255,cv2.THRESH_BINARY_INV)



# #Masking thresholded image from original image
# mask = cv2.bitwise_and(originalroi,originalroi,mask = thresh)



# #Finding contours for all infected regions
# contours,heirarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

# Infarea = 0
# for x in range(len(contours)):
# 	cv2.drawContours(originalroi,contours[x],-1,(0,0,255))
	
	
# 	#Calculating area of infected region
# 	Infarea += cv2.contourArea(contours[x])

# if Infarea > Tarea:
# 	Tarea = img.shape[0]*img.shape[1]

# try:
# 	per = 100 * Infarea/Tarea
# except ZeroDivisionError:
# 	per = 0


# result = {
#     "Perimeter": round(perimeter, 2),
#     "Total Area": round(Tarea, 2),
#     "Infected Area": round(Infarea, 2),
#     "Percentage of Infection Region": round(per, 2)
# }

# jsondata = json.dumps(result)
# print(jsondata)
# @app.route('/', methods=['GET'])
# def home():
#     return "Welcome to the image identification API!"

# @app.route('/identify', methods=['POST'])
# def identify():
#     try:
        
#     except Exception as e:
        

# if __name__ == "__main__":
#     app.run()
import base64
import json
import cv2
import numpy as np  
from flask import Flask, request, jsonify
import requests         
import argparse, sys, os

import pandas as pd
import requests

app = Flask(__name__)
def process_image(img):
    original = img.copy()
    neworiginal = img.copy()

    #Calculating number of pixels with shade of white(p) to check if exclusion of these pixels is required or not (if more than a fixed %) in order to differentiate the white background or white patches in image caused by flash, if present.
    p = 0 
    for i in range(img.shape[0]):
        for j in range(img.shape[1]):
            B = img[i][j][0]
            G = img[i][j][1]
            R = img[i][j][2]
            if (B > 110 and G > 110 and R > 110):
                p += 1
    #计算白色像素点的数量，根据其占总比例是否排除白色像素点，以防闪光灯等因素产生的干扰（变成灰色像素点大于10%）


    #finding the % of pixels in shade of white
    totalpixels = img.shape[0]*img.shape[1]
    per_white = 100 * p/totalpixels

    #excluding all the pixels with colour close to white if they are more than 10% in the image
    if per_white > 10:
        img[i][j] = [200, 200, 200]

    #Guassian blur
    #OpenCV（开源计算机视觉库）是一个非常常用和强大的工具。它提供了各种功能和算法，可以实现从简单的图像处理到复杂的计算机视觉任务，
    # 如边缘检测、轮廓检测、图像分割等。以下将对代码中使用到的几个OpenCV技术进行详细讲解。
    blur1 = cv2.GaussianBlur(img, (3, 3), 1)
    #高斯模糊，使图像更加平滑处理
    #mean-shift algo
    newimg = np.zeros((img.shape[0], img.shape[1], 3), np.uint8)
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    img = cv2.pyrMeanShiftFiltering(blur1, 20, 30, newimg, 0, criteria)
    #均值平移滤波，即在图像中存在一些不想要的像素点或者区域。这些噪声可能是来自于硬件设备或者环境等各种因素。
    #对周围的像素点进行均值计算
    #Guassian blur
    blur = cv2.GaussianBlur(img, (11, 11), 1)

    #Canny-edge detection
    canny = cv2.Canny(blur, 160, 290)
    canny = cv2.cvtColor(canny, cv2.COLOR_GRAY2BGR)

    #contour to find leafs
    bordered = cv2.cvtColor(canny, cv2.COLOR_BGR2GRAY)
    contours, hierarchy = cv2.findContours(bordered, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    maxC = 0
    for x in range(len(contours)):
        if len(contours[x]) > maxC:
            maxC = len(contours[x])
            maxid = x

    perimeter = cv2.arcLength(contours[maxid], True)
    Tarea = cv2.contourArea(contours[maxid])
    cv2.drawContours(neworiginal, contours[maxid], -1, (0, 0, 255))

    height, width, _ = canny.shape
    min_x, min_y = width, height
    max_x = max_y = 0
    frame = canny.copy()

    for contour, hier in zip(contours, hierarchy):
        (x, y, w, h) = cv2.boundingRect(contours[maxid])
        min_x, max_x = min(x, min_x), max(x+w, max_x)
        min_y, max_y = min(y, min_y), max(y+h, max_y)
        if w > 80 and h > 80:
            roi = img[y:y+h , x:x+w]
            originalroi = original[y:y+h , x:x+w]

    if (max_x - min_x > 0 and max_y - min_y > 0):
        roi = img[min_y:max_y , min_x:max_x]    
        originalroi = original[min_y:max_y , min_x:max_x]

    img = roi

    imghls = cv2.cvtColor(roi, cv2.COLOR_BGR2HLS)
    imghls[np.where((imghls==[30, 200, 2]).all(axis=2))] = [0, 200, 0]
    huehls = imghls[:,:,0]
    huehls[np.where(huehls==[0])] = [35]
    ret, thresh = cv2.threshold(huehls, 28, 255, cv2.THRESH_BINARY_INV)
    mask = cv2.bitwise_and(originalroi, originalroi, mask=thresh)
    contours, heirarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    Infarea = 0
    for x in range(len(contours)):
        cv2.drawContours(originalroi, contours[x], -1, (0, 0, 255))
        Infarea += cv2.contourArea(contours[x])

    if Infarea > Tarea:
        Tarea = img.shape[0]*img.shape[1]

    try:
        per = 100 * Infarea/Tarea
    except ZeroDivisionError:
        per = 0

    result = {
        "Perimeter": round(perimeter, 2),
        "Total Area": round(Tarea, 2),
        "Infected Area": round(Infarea, 2),
        "Percentage of Infection Region": round(per, 2)
    }

    return result


@app.route('/', methods=['GET'])
def home():
    return "Welcome to the image identification API!"


@app.route('/identify', methods=['POST'])
def identify():
    try:
        base64_img = request.get_json()['image']
        img_bytes = base64.b64decode(base64_img)
        np_arr = np.frombuffer(img_bytes, dtype=np.uint8)
        img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
        img = cv2.resize(img, ((int)(img.shape[1]/5), (int)(img.shape[0]/5)))
        
        result = process_image(img)
        
        jsondata = json.dumps(result)
        return jsondata, 200
    except Exception as e:
        return str(e), 400


if __name__ == "__main__":
    app.run()
#当涉及到图像处理和计算机视觉任务时，OpenCV（开源计算机视觉库）是一个非常常用和强大的工具。
# 它提供了各种功能和算法，可以实现从简单的图像处理到复杂的计算机视觉任务，如去除噪声，边缘检测、轮廓检测、图像分割等。
#cv2.GaussianBlur，高斯模糊，使图像更加平滑
#cv2.pyrMeanShiftFiltering函数对图像去除噪声功能，图像有些像素点是找照片误差造成的，我们用周围像素的均值来取代点
#cv2.canny 边缘检测是一种经典的边缘检测算法，被广泛应用于计算机视觉任务中。
#cv2.findContours函数对Canny边缘图像进行轮廓检测，找出所有叶子的轮廓并画出边界
# 它可以有效地检测图像中的边缘，并且具有抑制噪声的能力。
# 在这段代码中，使用cv2.Canny函数对高斯模糊后的图像进行边缘检测
#将输入的图像进行高斯模糊处理，以便消除噪声并平滑化图像。
# 通过均值平移算法将图像压缩到几个颜色区域，并生成一个新的图像。
# 再次将新图像进行高斯模糊处理，并使用Canny边缘检测算法检测边缘。
# 使用轮廓检测算法找到所有叶子，并画出边界。
# 选择具有最大周长的轮廓（通常是整个图像的周长）作为最外层轮廓。
# 计算最大轮廓的周长和面积。
# 在未经处理的原始图像中，根据色度值找到潜在感染区域。HSV（色相饱和度亮度）或HLS（色相亮度饱和度）颜色空间经常用于此目的。
# 统计所有感染区域的总面积，并计算其与整个叶子的面积之比，确定感染的百分比。
#根据颜色特征来定位感染区域。


