from apisModule import getApiData
from mashupsModule import getMATData
from sampleSourceCodeModule import getSSCData
from sdksModule import getSDKsData
from commonMethod import getProperties, initProperties

def function():
    # 四个名字对应四个不同的.txt文件，加载.txt文件中的位置信息，从相应的位置开始爬取数据
    # properties = getProperties()
    # url = properties.get('apis.url')+"="+properties.get('apis.page')
    # print(url)
    execute(getProperties())

def execute(p):
    # flag判断是否需要进行定制化操作
    flag = p.get('flag')
    if flag.__eq__('true'):
        print("定制化抓取操作")
        # apis模块定制操作
        apisFlag = p.get('apis.flag')
        if apisFlag.__eq__('true'):
            runApis(p)
        # mashups模块的定制化操作
        mashupsFlag = p.get('mashups.flag')
        if mashupsFlag.__eq__('true'):
            runMashups(p)

        # sampleSourceCode模块的定制化操作
        sampleSourceCodeFlag = p.get('sampleSourceCode.flag')
        if sampleSourceCodeFlag.__eq__('true'):
            runSampleSourceCode(p)
        sdksFlag = p.get('sdks.flag')
        if sdksFlag.__eq__('true'):
            runSdks(p)
    else:
        # apis模块
        print("抓取所有模块数据")
        runApis(p)
        # mashups模块
        runMashups(p)
        #sampleSourceCode模块
        runSampleSourceCode(p)
        #sdks模块
        runSdks(p)
        print("所有数据抓取完毕")
    initProperties()


def runApis(p):
    print("抓取apis模块")
    name = p.get('apis.name')
    url = p.get('apis.url') + "=" + p.get('apis.page')
    page = int(p.get('apis.page'))
    count = int(p.get('apis.count'))
    getApiData(name, url, page, count)

def runMashups(p):
    print("抓取mashups模块")
    name = p.get('mashups.name')
    url = p.get('mashups.url') + "=" + p.get('mashups.page')
    page = int(p.get('mashups.page'))
    count = int(p.get('mashups.count'))
    getMATData(name, url, page, count)

def runSampleSourceCode(p):
    print("抓取SampleSource模块")
    name = p.get('sampleSourceCode.name')
    url = p.get('sampleSourceCode.url') + "=" + p.get('sampleSourceCode.page')
    page = int(p.get('sampleSourceCode.page'))
    count = int(p.get('sampleSourceCode.count'))
    getSSCData(name, url, page, count)

def runSdks(p):
    print("抓取sdks模块")
    name = p.get('sdks.name')
    url = p.get('sdks.url')+"="+p.get('sdks.page')
    page = int(p.get('sdks.page'))
    count = int(p.get('sdks.count'))
    getSDKsData(name, url, page, count)


if __name__ == "__main__":
    function()


# #选择方法运行
# def selectMethodToRun(fileName):
#     mashupsInfo = getPositionInfo(fileName)
#     name = mashupsInfo['name']
#     apiUrl = mashupsInfo['apiUrl']
#     page = mashupsInfo['page']
#     count = mashupsInfo['count']
#     if name == 'apisData':
#         getApiData(apiUrl, page, count)
#     if name == 'MashupsData':
#         getMATData(apiUrl, page, count)
#     if name == 'SampleSourceCodeData':
#         getSSCData(apiUrl, page, count)
#     if name == 'SDKsData':
#         getSDKsData(apiUrl, page, count)
#     print("所有的数据均已经爬取完毕")

# # 当所有的数据读取完毕之后，重置配置文件
# def reStoreConfigFile():
#     fileNames = ['apis', 'mashups', 'sampleSourceCode', 'sdks']
#     count = 0
#     page = 0
#     for fileName in fileNames:
#         # if fileName == 'apis':
#         #     apiUrl = 'https://www.programmableweb.com/category/all/apis'
#         #     savePositionInfo('apisData', apiUrl, page, count, fileName)
#         # if fileName == 'mashups':
#         #     apiUrl = 'https://www.programmableweb.com/category/all/mashups'
#         #     savePositionInfo('MashupsData', apiUrl, page, count, fileName)
#         if fileName == 'sampleSourceCode':
#             apiUrl = 'https://www.programmableweb.com/category/all/sample-source-code'
#             savePositionInfo('SampleSourceCodeData', apiUrl, page, count, fileName)
#         if fileName == 'sdks':
#             apiUrl = 'https://www.programmableweb.com/category/all/sdk'
#             savePositionInfo('SDKsData', apiUrl, page, count, fileName)
#     print("配置文件重置成功")


