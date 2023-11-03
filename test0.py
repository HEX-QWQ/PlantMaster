# from flask import Flask, request, jsonify
# import requests
# import base64

# app = Flask(__name__)

# def Get_Token(client_id, client_secret):
#     try:
#         url_token = f'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials' \
#                     f'&client_id={client_id}&client_secret={client_secret}'
#         respond = requests.post(url_token)
#         token = respond.json()["access_token"]
#         return token
#     except Exception as e:
#         raise Exception("获取Token失败！")

# def Identify_image(token, image_url):
#     try:
#         response = requests.get(image_url)
#         img = base64.b64encode(response.content)
#         params = {"image": img}
#         headers = {'content-type': 'application/x-www-form-urlencoded'}
#         url_Identify_image = f'https://aip.baidubce.com/rest/2.0/image-classify/v1/plant?access_token={token}'
#         respond = requests.post(url_Identify_image, data=params, headers=headers)
#         data = respond.json()
#         if respond.status_code == 200:
#             return data
#         else:
#             raise Exception("请求响应失败！")
#     except Exception as e:
#         raise Exception("识别图片失败！")

# @app.route('/', methods=['GET'])
# def home():
#     return "Welcome to the image identification API!"

# @app.route('/identify', methods=['POST'])
# def identify():
#     try:
#         # 获取API Key和Secret Key
#         loginName = "kmScXDXEfGVOZsuUDq5UfCdj"
#         password = "N9VbV1grWRgwGo5ng02T9ih0w9T4lfjr"
        
#         # 获取Token
#         token = Get_Token(loginName, password)
        
#         # 获取POST请求中的图片链接
#         image_url = request.json['image_url']
        
#         # 进行图片识别
#         result = Identify_image(token, image_url)
        
#         return jsonify(result)
#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

# if __name__ == "__main__":
#     app.run()
from flask import Flask, request, jsonify
import requests
import base64

app = Flask(__name__)

def Get_Token(client_id, client_secret):
    try:
        url_token = f'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials' \
                    f'&client_id={client_id}&client_secret={client_secret}'
        respond = requests.post(url_token)
        token = respond.json()["access_token"]
        return token
    except Exception as e:
        raise Exception("获取Token失败！")

def Identify_image(token, image_base64):
    try:
        print(image_base64)
        params = {"image": image_base64}
        headers = {'content-type': 'application/x-www-form-urlencoded'}
        url_Identify_image = f'https://aip.baidubce.com/rest/2.0/image-classify/v1/plant?access_token={token}'
        respond = requests.post(url_Identify_image, data=params, headers=headers)
        data = respond.json()
        if respond.status_code == 200:
            return data
        else:
            raise Exception("请求响应失败！")
    except Exception as e:
        raise Exception("识别图片失败！")

@app.route('/', methods=['GET'])
def home():
    return "Welcome to the image identification API!"

@app.route('/identify', methods=['POST'])
def identify():
    try:
        # 获取API Key和Secret Key
        loginName = "kmScXDXEfGVOZsuUDq5UfCdj"
        password = "N9VbV1grWRgwGo5ng02T9ih0w9T4lfjr"

        # 获取Token
        token = Get_Token(loginName, password)

        # 获取POST请求中的base64编码图像数据
        image_base64 = request.json['image_base64']

        # 进行图片识别
        result = Identify_image(token, image_base64)

        return jsonify(result)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run()