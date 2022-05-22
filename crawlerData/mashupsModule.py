from commonMethod import *


# 爬取Mashups数据
def getMATData(fileName, apiUrl, page, count):
    # apiUrl = baseUrl + "/category/all/mashups?page=" + str(10)
    print(apiUrl)

    pageBS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)

    # 确定总页数
    try:
        totalPageATag = pageBS.select('div.text-center > ul > li.pager-next > a')[0]
    except:
        return
    findTotalPage = re.compile(r'<a .*?>(\d*?)</a>')
    totalPage = int(re.findall(findTotalPage, str(totalPageATag))[0])
    workBookName = fileName
    sheetName = fileName
    # if page == 0:
    # wb = Workbook()
    # ws = wb.active
    # ws.title = u'Mashups数据'
    # fileName = 'mashups'
    for i in range(page, totalPage):
        tbody = pageBS.select('table > tbody > tr')
        # 为了节约时间，就先拿两条数据
        for item in tbody:
            # for j in range(10, 13):
            #     item = tbody[j]
            try:
                item = str(item)
                # 拿到Submitted Date
                findSubmittedDate = re.compile(r'<td class=".*?views-field-created .*?">(.*?)</td>')
                submittedDate = re.findall(findSubmittedDate, item)

                findLink = re.compile(r'<a href="(.*?)"')
                link = re.findall(findLink, item)[0]
                # 链接到具体的页面
                url = baseUrl + link
                print(url)
                # 拿到具体表单的html
                innerBS = isNeedReSendUrl(getHtml(url), url)
                # if innerBS is None:
                #     count = count + 1
                #     continue
                # 得到封装好的表单数据
                singleData = getFormData(innerBS)
                singleData['submitted_date'] = partitionedArray(submittedDate).replace(" ", "")
                print(singleData)
                # 保存爬取到的数据，保存数据到excel
                saveDataInExcel(singleData, count, workBookName, sheetName)
                # 保存数据到excel
                # saveDataInCSV(singleData, fileName, count)
            except Exception as result:
                print("没有找到这个数据，继续找下一条")
            #
            finally:
                count = count + 1
        # 当循环到最后，可能没有下一页啦，下面代码还要再执行一次，容易陷入死循环，所以需要加个判断条件
        if i == totalPage:
            break
        # 下一页的url地址
        apiUrl = baseUrl + "/category/all/mashups?page=" + str(i + 1)
        print(apiUrl)
        pageBS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
        # 爬取完整页数据后保存一下当前的位置信息
        savePositionInfo('mashups', str(i + 1), str(count))
    # 把excel数据转换成csv
    excelToCSV(fileName)


def getFormData(BS):
    topContent = BS.select(r'header > div.profile-top.col-sm-12.col-md-8.col-lg-8')
    mashupName = getMashupName(topContent)
    description = getDescription(topContent)
    # 一起得到specs区域需要的四个值
    specsContent = BS.select('#tabs-content > div.section.specs')
    specsContentFormData = getSpecsContentFormData(specsContent)
    # follower存在一些处理后的参数，使用拼接的手法好像不行
    # followersUrl = baseUrl + "/mashup/" + mashupName + "/followers"
    # #https://www.programmableweb.com/mashup/seo-auto%E2%80%91tagger/followers   实际的url地址
    # #https://www.programmableweb.com/mashup/seo-auto‑tagger/followers  拼接得到自己拼接
    followersLinkTag = BS.select('#myTab > li:nth-child(2) > a')
    findFollowersLink = re.compile(r'<a .*? href="(.*?)"')
    followersLink = re.findall(findFollowersLink, str(followersLinkTag))[0]

    followers = getFollowers(followersLink)
    singleData = {
        "mashups_name": mashupName,
        "related_apis": specsContentFormData['relatedAPIs'],
        "description": description,
        "categories": specsContentFormData['categories'],
        "submitted_date": "",
        "mashup_app_type": specsContentFormData['Mashup/AppType'],
        "company": specsContentFormData['company'],
        "followers_numbers": followers['FollowersNumbers'],
        "followers_names": followers['FollowersNames']
    }
    return singleData


# # 获取html代码
# def getHtml(url):
#     try:
#         response = urllib.request.urlopen(url, timeout=50)
#         html = response.read().decode("utf-8")
#         BS = BeautifulSoup(html, "html.parser")
#         return BS
#     except HTTPError as e:
#         print("获取html失败，请先检查网络是否通畅")
#         if hasattr(e, "code"):
#             print(e.code)
#         if hasattr(e, "reason"):
#             print(e.reason)
#         return None


row2 = 0
wb = Workbook()
ws = wb.active
ws.title = u'Mashups数据'


# def saveDataInExcel(singleData):
#     global row2
#     col = 0
#     for key in singleData.keys():
#         if row2 == 0:
#             ws.cell(row2 + 1, col + 1).value = key
#             ws.cell(row2 + 2, col + 1).value = singleData[key]
#             col = col + 1
#         else:
#             ws.cell(row2 + 2, col + 1).value = singleData[key]
#             col = col + 1
#     row2 = row2 + 1
#     print("row的值:" + str(row2))
#
#     wb.save('./data/mashupsData.xlsx')


def getMashupName(topContent):
    findMashupName = re.compile(r'<h1>Mashup: (.*?)</h1>', re.M)
    mashupName = re.findall(findMashupName, str(topContent))[0]
    return mashupName


def getDescription(topContent):
    findDescription = re.compile(r'<div class="field-item even">([\s\S]*?)</div>', re.M)
    description = re.findall(findDescription, str(topContent))[0]
    return description


def getSpecsContentFormData(specsContent):
    findFields = re.compile(r'<div class="field">[\s\S]*?</div>')
    fields = re.findall(findFields, str(specsContent))
    findValue = re.compile(r'<a href=".*?">(.*?)</a>')
    relatedAPIs = re.findall(findValue, fields[0])

    categories = re.findall(findValue, fields[1])
    findCompany = re.compile(r'Company[\s\S]*?<span>(.*?)<')
    company = re.findall(findCompany, str(fields))
    findMAT = re.compile(r'Mashup[\s\S]*?<span>(.*?)<')
    MAT = re.findall(findMAT, str(fields))

    # #有些数据没有company
    # flag = str(fields).find("Company")
    # flag2 = str(fields).find("Company")
    # #因为后面两个数据没有a标签，所有不能用a标签去进行相应的匹配
    # findValue2 = re.compile(r'<span>(.*?)</span>')
    # if flag != -1:
    #     company = re.findall(findValue2, fields[3])[0]
    #     MAT = re.findall(findValue2, fields[4])[0]
    # else:
    #     company = "不存在该值"
    #     MAT = re.findall(findValue2, fields[3])[0]
    return {"relatedAPIs": subStringAmp(partitionedArray(relatedAPIs)), "categories": partitionedArray(categories),
            "company": partitionedArray(company), "Mashup/AppType": partitionedArray(MAT)}


def getFollowers(followersLink):
    followersUrl = baseUrl + followersLink
    print(followersUrl)
    followersBS = isNeedReSendUrl(getHtml(followersUrl), followersUrl)
    content = followersBS.select('#followers')
    followersNumberTag = followersBS.select('#followers > div.block-title > span')
    findFollowersNumber = re.compile(r'(\d+)')
    followersNumber = int(re.findall(findFollowersNumber, str(followersNumberTag))[0])
    if followersNumber > 0:
        # 找到所有的username
        findFollowersUserNames = re.compile(r'<a class="username" .*?>(.*?)</a>')
        followersUserNames = re.findall(findFollowersUserNames, str(content))
    else:
        followersUserNames = []
    return {
        "FollowersNumbers": str(followersNumber),
        "FollowersNames": partitionedArray(followersUserNames)
    }

# def partitionedArray(array):
#     return "###".join(array)
# #amp;
# def subStringAmp(value):
#     return str(value).replace("amp;", "")

# #检测是否需要重发，网络不稳定是进行重发
# def isNeedReSendUrl(BS, url):
#     #休眠的初始值为10
#     second = 10
#     while BS is None:
#         #设置一个休眠时间，等一会儿再发
#         print("休眠"+str(second)+"后重发:"+url)
#         time.sleep(second)
#         BS = getHtml(url)
#         second = second + 5
#     return BS
