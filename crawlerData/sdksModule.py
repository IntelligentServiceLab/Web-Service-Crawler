from commonMethod import *
def getSDKsData(fileName, apiUrl, page, count):
    # apiUrl = baseUrl + "/category/all/sdk"
    print(apiUrl)
    BS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
    #确定总页数
    try:
        totalPageATag = BS.select('div.text-center > ul > li.pager-next > a')
    except:
        return
    findTotalPage = re.compile(r'<a .*?>(\d*?)</a>')
    totalPage = int(re.findall(findTotalPage, str(totalPageATag))[0])
    # wb = Workbook()
    # ws = wb.active
    # ws.title = u'SDKs数据'
    workBookName = fileName
    sheetName = fileName
    # count = 0
    # fileName = "sdks"
    for i in range(page, totalPage + 1):
        tbody = BS.select('table > tbody > tr')
        # for j in range(0, 2):
        #     item = tbody[j]
        for item in tbody:
            try:
                item = str(item)
                findLink = re.compile(r'<a href="(.*?)"')
                link = re.findall(findLink, item)[0]
                url = baseUrl + link
                print(url)
                innerBS = isNeedReSendUrl(getHtml(url), url)
                # if innerBS is None:
                #     # 给他空出一行来，为了当时候可以在表格中清晰看到
                #     count = count + 1
                #     continue
                singleData = getFormData(innerBS)
                print(singleData)
                saveDataInExcel(singleData, count, workBookName, sheetName)
            except Exception as result:
                print(result)
                print("没有找到这条数据，找一条")
            finally:
                count = count + 1

        if i == totalPage:
            break
        apiUrl = baseUrl + '/category/all/sdk?page=' + str(i+1)
        BS = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
        savePositionInfo('sdks', str(i+1), str(count))
        print(apiUrl)
    # 把excel数据转换成csv
    excelToCSV(fileName)

# 用来获取具体的表单数据
def getFormData(BS):
    name = getName(BS)
    description = getDescription(BS)
    specFormData = getSPECSFormData(BS)
    # print(specFormData)
    followerATag = BS.select('#myTab > li:nth-child(2) > a')
    findFollowersLink = re.compile(r'<a .*? href="(.*?)"')
    followersLink = re.findall(findFollowersLink, str(followerATag))[0]
    followers = getFollowers(followersLink)
    return {
        "sdks_name": name,
        "description": description,
        "related_apis": specFormData['Related APIs'],
        "related_platform_languages": specFormData['Related Platform / Languages'],
        "categories": specFormData['Categories'],
        "added_date": specFormData['Added Date'],
        "sdk_provider": specFormData['SDK Provider'],
        "followers_number": followers['FollowersNumbers'],
        "followers": followers['FollowersNames']
    }
# 获取名称
def getName(BS):
    content = BS.select('div.node-header > h1')[0]
    findName = re.compile(r'>(.*?)<')
    name = re.findall(findName, str(content))
    return partitionedArray(name).replace(" SDK", "")

# 获取描述信息
def getDescription(BS):
    content = BS.select('#tabs-header-content > div > div.tabs-header_description')
    findDescription = re.compile(r'> (.*?)<')
    description = re.findall(findDescription, str(content))
    return partitionedArray(description)

def getSPECSFormData(BS):
    content = str(BS.select('#tabs-content > div.section.specs'))
    # # 找到RelatedApis,先找到里面所有的a标签
    relatedApis = getSPECSFormData2('Related APIs', content)
    # print(partitionedArray(relatedApis).replace("amp;", "", 1))
    # # 找到Related Platform / Languages,同理也是先找到里面的a标签
    rpl = getSPECSFormData2('Related Platform / Languages', content)
    # # 找到Categories,先找到里面所有的a标签
    categories = getSPECSFormData2('Categories', content)
    # print(categories)
    # 找到Added的值
    findAdded = re.compile(r'<label>Added</label> <span>(.*?)</span>')
    added = re.findall(findAdded, content)
    # print(added)
    # 找到Version字段的值
    findVersion = re.compile(r'<label>Version</label> <span>(.*?)</span>')
    version = re.findall(findVersion, content)
    # print(version)
    # 找到SDK Provider,先找到里面所有的a标签
    sp = getSPECSFormData2('SDK Provider', content)
    return{
        "Related APIs": partitionedArray(relatedApis).replace("amp;", "", 1).replace(" API", ""),
        "Related Platform / Languages": partitionedArray(rpl),
        "Categories": partitionedArray(categories),
        "Added Date": partitionedArray(added),
        "Versions": version,
        "SDK Provider": partitionedArray(sp)
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
