from commonMethod import *
def getSSCData(fileName, apiUrl, page, count):
    # apiUrl = baseUrl + "/category/all/sample-source-code"
    print(apiUrl)
    BS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
    #确定总页数
    try:
        totalPageATag = BS.select('div.text-center > ul > li.pager-next > a')
        findTotalPage = re.compile(r'<a .*?>(\d*?)</a>')
        totalPage = int(re.findall(findTotalPage, str(totalPageATag))[0])
    except:
        return

    # wb = Workbook()
    # ws = wb.active
    # ws.title = u'SampleSourceCode数据'
    workBookName = fileName
    sheetName = fileName
    # count = 0  # 统计行数
    # fileName = 'sample_source_code'
    # 这层循环用来控制页数,totalPage先不使用
    for i in range(page, totalPage + 1):
        tbody = BS.select('table > tbody > tr')
        # 这层循环用来拿每一页数据,然后每一个数据对应的具体数据,先控制在三个
        # for j in range(0, 2):
        #     item = tbody[j]
            # 这行代码表示全拿的意思
        for item in tbody:
            try:
                item = str(item)
                findLink = re.compile(r'<a href="(.*?)"')
                link = re.findall(findLink, item)[0]
                # 链接到具体的页面
                url = baseUrl + link
                print(url)
                innerBS = isNeedReSendUrl(getHtml(url), url)
                # if innerBS is None:
                #     # 给他空出一行来，为了当时候可以在表格中清晰看到
                #     count = count + 1
                #     continue
                singleData = getFormData(innerBS)
                print(singleData)
                # 保存爬取到的数据，保存数据到excel
                saveDataInExcel(singleData, count, workBookName, sheetName)
            except Exception as result:
                print(result)
                print("没有找到这条数据，找一条")
            finally:
                count = count + 1

        if i == totalPage:
            break
        # 下一页的url地址
        apiUrl = baseUrl + "/category/all/sample-source-code?page=" + str(i+1)
        savePositionInfo('sampleSourceCode', str(i + 1), str(count))
        print(apiUrl)
        BS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
    excelToCSV(fileName)


# 拿到具体的表单数据
def getFormData(BS):
    name = getName(BS)
    description = getDescription(BS)
    specsFormData = getSPECSFormData(BS)
    # print(specsFormData)
    followerATag = BS.select('#myTab > li:nth-child(2) > a')
    findFollowersLink = re.compile(r'<a .*? href="(.*?)"')
    followersLink = re.findall(findFollowersLink, str(followerATag))[0]
    followers = getFollowers(followersLink)
    singleData = {
        "sample_source_code_name": name,
        "description": description,
        "related_apis": specsFormData['RelatedAPIs'],
        "related_platform_languages": specsFormData['Related Platform / Languages'],
        "categories": specsFormData['Categories'],
        "added_date": specsFormData['Added'],
        "versions": specsFormData['Versions'],
        "sample_source_code_provider": specsFormData['Sample Source Code Provider'],
        "followers_number": followers['FollowersNumbers'],
        "followers": followers['FollowersNames']

    }
    return singleData


# 获得名称
def getName(BS):
    content = BS.findAll(name='span', attrs={'class': "inline odd last"})[0]
    findName = re.compile(r'>(.*?)<')
    name = re.findall(findName, str(content))
    return partitionedArray(name)


# 获得描述信息
def getDescription(BS):
    content = BS.select('#tabs-header-content > div > div.tabs-header_description')
    findDescription = re.compile(r'>(.*?)<')
    description = re.findall(findDescription, str(content))
    return partitionedArray(description)[1:]


# 获得SPECS整个表单数据
def getSPECSFormData(BS):
    content = str(BS.select('#tabs-content > div.section.specs'))
    # print(content)
    # 找到a标签里面所包含的文本值
    # findATagContent = re.compile(r'<a .*?>(.*?)</a>')
    # # 找到RelatedApis,先找到里面所有的a标签
    relatedApis = getSPECSFormData2('Related APIs', content)
    # print(relatedApis)
    # findRelatedApisATag = re.compile(r'Related APIs</label> <span>([\s\S]*?)</span>')
    # relatedApisATag = re.findall(findRelatedApisATag, str(content))
    # relatedApis = re.findall(findATagContent, str(relatedApisATag))
    # # print(relatedApis)
    # # 找到Related Platform / Languages,同理也是先找到里面的a标签
    rpl = getSPECSFormData2('Related Platform / Languages', content)
    # print(rpl)
    # findRPLATag = re.compile(r'Related Platform / Languages</label> <span>([\s\S]*?)</span>')
    # rplATag = re.findall(findRPLATag, content)
    # rpl = re.findall(findATagContent, str(rplATag))
    # print(rpl)

    # # 找到Categories,先找到里面所有的a标签
    categories = getSPECSFormData2('Categories', content)
    # print(categories)
    # findCategoriesATags = re.compile(r'Categories</label> <span>([\s\S]*?)</span>')
    # categoriesATags = re.findall(findCategoriesATags, content)
    # categories = re.findall(findATagContent, str(categoriesATags))

    # 找到Added的值
    findAdded = re.compile(r'<label>Added</label> <span>(.*?)</span>')
    added = re.findall(findAdded, content)
    # print(added)

    # 找到Version字段的值
    findVersion = re.compile(r'<label>Version</label> <span>(.*?)</span>')
    version = re.findall(findVersion, content)
    # print(version)

    # 找到Sample Source Code Provider,先找到里面所有的a标签
    sscp = getSPECSFormData2('Sample Source Code Provider', content)
    # findSSCPATag = re.compile(r'Sample Source Code Provider</label> <span>([\s\S]*?)</span>')
    # sscpATAg = re.findall(findSSCPATag, content)
    # sscp = re.findall(findATagContent, str(sscpATAg))
    # print(sscp)
    return {
        "RelatedAPIs": partitionedArray(relatedApis).replace(" API", ""),
        "Related Platform / Languages": partitionedArray(rpl),
        "Categories": partitionedArray(categories),
        "Added": partitionedArray(added),
        "Versions": partitionedArray(version),
        "Sample Source Code Provider": partitionedArray(sscp)
    }


# 由于拿取SPECS表单的每个字段有太多的共同之处，所以提取一个公共的方法
def getSPECSFormData2(field, content):
    findATagContent = re.compile(r'<a .*?>(.*?)</a>')
    regx = field + '</label> <span>([\s\S]*?)</span>'
    findTempATag = re.compile(regx)
    tempATag = re.findall(findTempATag, content)
    arrays = re.findall(findATagContent, str(tempATag))
    return arrays


def getFollowers(followersLink):
    followersUrl = baseUrl + followersLink
    print(followersUrl)
    followersBS = isNeedReSendUrl(getHtml(followersUrl), followersUrl)
    content = followersBS.select('#followers')
    followersNumberTag = followersBS.select('#followers > div.block-title > span')
    # 有些地方没有followers标签
    if len(followersNumberTag) == 0:
        return {
            "FollowersNumbers": "不存在该值",
            "FollowersNames": "不存在该值"
        }
    # print(followersNumberTag)
    findFollowersNumber = re.compile(r'(\d+)')
    followersNumber = int(re.findall(findFollowersNumber, str(followersNumberTag))[0])
    # print(followersNumber)
    if followersNumber > 0:
        # 找到所有的username
        findFollowersUserNames = re.compile(r'<a class="username" .*?>(.*?)</a>')
        followersUserNames = re.findall(findFollowersUserNames, str(content))
        # print(followersUserNames)
    else:
        followersUserNames = []
    return {
        "FollowersNumbers": str(followersNumber),
        "FollowersNames": partitionedArray(followersUserNames)
    }


# 保存数据到excel中
# row3 = 0
# wb = Workbook()
# ws = wb.active
# ws.title = u'SampleSourceCode数据'


# def saveDataInExcel(singleData):
#     global row3
#     col = 0
#     for key in singleData.keys():
#         if row3 == 0:
#             ws.cell(row3 + 1, col + 1).value = key
#             ws.cell(row3 + 2, col + 1).value = singleData[key]
#             col = col + 1
#         else:
#             ws.cell(row3 + 2, col + 1).value = singleData[key]
#             col = col + 1
#     row3 = row3 + 1
#     print("row的值:" + str(row3))
#
#     wb.save('./data/SampleSourceCode.xlsx')
