import re

from commonMethod import *

# baseUrl = "https://www.programmableweb.com"
def getApiData(fileName, apiUrl, page, count):
    print(apiUrl)

    workBookName = fileName
    sheetName = fileName
    # fileName = "apis"
    while True:
        # 数据太多，拿两个页面数据就跳出循环,如果需要全部数据可以把下面两行代码注释掉即可
        bs = isNeedReSendUrl(getHtml(apiUrl), apiUrl)
        if bs is None:
            break
        # 这里得加一个判断，如果没有下一页，就结束循环
        nextPageContent = bs.select('ul > li.pager-last > a')
        if len(nextPageContent) == 0:
            print("没有下一页啦")
            break

        findNextPage = re.compile(r'<a href="(.*?)"')
        nextPage = re.findall(findNextPage, str(nextPageContent))[0]
        tbody = bs.select("table > tbody >tr")
        # 设置匹配规则
        # 1.找到超链接
        findLink = re.compile(r'<a href="(.*?)">')
        for item in tbody:
        # # # 为了节约时间，先拿1条
        # for i in range(9, 11):
        #     item = tbody[i]
            # 有些超链接拿到了，但是无法访问，为了处理这种特殊情况所以得加上这个
            # if bs is None:
            #     # 给他空出一行来，为了当时候可以在表格中清晰看到
            #     count = count + 1
            #     continue
            # 增加一个异常处理
            try:
                item = str(item)
                link = re.findall(findLink, item)[0]
                # 拿到api，然后拼接得到具体页面的html信息
                url = baseUrl + link
                print(url)
                bs = isNeedReSendUrl(getHtml(url), url)
                singleData = getFormData(bs)
                # 得到版本字段
                versionValues = getVersionValues(item)
                singleData['Versions'] = partitionedArray(versionValues)
                # 保存爬取到的数据，保存数据到excel
                saveDataInExcel(singleData, count, workBookName, sheetName)

                print(singleData)
            except Exception as result:
                print(result)
                print("没有找到这个api，找下一个")
            finally:
                count = count + 1
        # 拼凑形成新的apiUrl，即下一页的url地址
        apiUrl = baseUrl + nextPage
        print(apiUrl)
        page = page + 1
        savePositionInfo('apis', str(page), str(count))
    # 把excel数据转换成csv
    excelToCSV(fileName)




# 获得所有数据的方法
def getFormData(bs):
    content = str(bs.select("body > div.main-container.container > div:nth-child(2) > section > div.region.region-content"))
    apiName = getApiName(content)
    categories = getCategories(content)
    description = getDescription(content)
    submittedDate = getSubmitted(content)
    developers = getDevelopers(bs)
    followers = getFollowers(bs)
    singleData = {
        "api_name": apiName,
        "description": description,
        "categories": categories,
        "submitted_date": submittedDate,
        "developers_numbers": developers['developersNumbers'],
        "developers_name": developers['developersNames'],
        "developers_m_r": developers['developersMR'],
        "followers_numbers": followers['followersNumber'],
        "followers_name": followers['followersUserNames'],
        "versions": []
    }

    return singleData


# 获取版本字段号
def getVersionValues(item):
    # 找version字段的值
    findVersion = re.compile(r'<td class=".*? views-field-pw-version-links">(.*?)</td>')
    findVersionA = re.compile(r'>(.*)<')
    findVersionB = re.compile(r'<span class="style">(.*?)<span')
    version = re.findall(findVersion, item).pop()
    # 版本只有一个a标签，若有div证明是一个下拉框
    flag = version.find("div")
    versionValues = []
    # 只有一个值
    if flag == -1:
        versionValues = re.findall(findVersionA, version)
        # print(str(versionValues))
    else:
        # 如果有多个值
        versionValues = re.findall(findVersionB, version)
        for i in range(len(versionValues)):
            # 得到这个形式['FEED', 'REST', 'RESTv2'],还需要REST和v2分开
            versionValues[i] = versionValues[i].replace("</span>", "").replace(" ", "").replace("v", " v")
    return versionValues

# 找到apiName字段
def getApiName(content):
    # '#node-63955 > header > div.profile-top.col-sm-12.col-md-8.col-lg-8 > div.node-header > div.master_record_div > h1'
    findApiName = re.compile(r'<div class="master_record_div"><h1>(.*?) <span')
    apiName = re.findall(findApiName, content)[0]
    return str(apiName).replace(" API", "").replace("amp;", "", 1)

    # 记住最后需要把拿取每个字段的代码单独抽取出来

# 找到apiName字段
def getCategories(content):

    findATag = re.compile(r'<div class="tags">(.*?)</div>')
    findCategories = re.compile(r'<a .*?>(.*?)</a>')
    aTag = str(re.findall(findATag, content))
    categories = re.findall(findCategories, aTag)
    # print(categories)
    return partitionedArray(categories)

# 找到描述字段
def getDescription(content):
    findDescription = re.compile(r'<div class="api_description tabs-header_description">([\s\S]*?)</div>')
    description = re.findall(findDescription, content)
    aHead = re.compile('<a href=".*?">')
    aTail = re.compile('</a>')
    description2 = re.sub(aHead, "", str(description))
    description3 = re.sub(aTail, "", str(description2))[3:-2]
    return description3

# 拿到submitted的日期值
def getSubmitted(content):
    findSubmitted = re.compile('<div class="version pull-left col-lg-2">.*?</span>(.*?)</div>')
    submitted = re.findall(findSubmitted, content)
    submittedDate = []
    for time1 in submitted:
        try:
            time.strptime(time1, "%m.%d.%Y")

            submittedDate.append(time1)
        except:
            pass
    return partitionedArray(submittedDate)

# 得到跳往developers的链接
def getDevelopers(bs):
    developersATag = bs.findAll('a', attrs={'class': "developers"})
    findDevelopersLink = re.compile(r'<a.*?href="(.*?)"')
    developersLink = re.findall(findDevelopersLink , str(developersATag))[0]
    developersUrl = baseUrl + developersLink
    print(developersUrl)
    #得到developers链接之后，跳转，拿到连接之后的内容
    developersBs = isNeedReSendUrl(getHtml(developersUrl), developersUrl)
    # 点进去之后拿数字
    developersNumbersTag = developersBs.select('#developers > div.block-title > span')
    findDevelopersNumbers = re.compile(r'(\d+)')
    developersNumbers2 = re.findall(findDevelopersNumbers, str(developersNumbersTag))
    developersNames = []
    developersMR = []
    # 设置一个变量，空值抓取页面数，以此达到演示作用即可，如果不需要拿到全部数据
    # k = 0
    # 拿取表单的内容
    while True:
        # k = k + 1
        # if k == 3:
        #     break
        # findDevelopersTable = re.compile(r'<table class="views-table cols-2 table">[\s\S]*?</table>')
        # developersTable = str(re.findall(findDevelopersTable, str(developersBs)))
        developersTable = str(developersBs.select('div.view-content > table'))
        # 获取表单中name的值和Mashup / Resource的值
        findDevelopersNames = re.compile(r'<a class="username.*?">(.*?)</a>')
        developersNames2 = re.findall(findDevelopersNames, developersTable)

        # Mashup/Resource用MR替代
        findDevelopersMR = re.compile(r'<a href="/mashup.*?">(.*?)</a>')
        developersMR2 = re.findall(findDevelopersMR, developersTable)
        for i in range(0, len(developersNames2)):
            # print(i)
            developersNames.append(developersNames2[i])
            developersMR.append(developersMR2[i])

        # 点击加载更多得到更多数据
        loadMoreATag = developersBs.select('#pager_id_list_all')
        if len(loadMoreATag) == 0:
            break
        findLoadLink = re.compile('<a class=".*?" href="(.*?)"')
        developersLink = re.findall(findLoadLink, str(loadMoreATag))[0]
        developersUrl = str(baseUrl + developersLink).replace("amp;", "")
        print(developersUrl)

        developersBs = isNeedReSendUrl(getHtml(developersUrl), developersUrl)


    return {"developersNumbers": partitionedArray(developersNumbers2), "developersNames": partitionedArray(developersNames),
            "developersMR": partitionedArray(developersMR)}


# 拿到followers字段
def getFollowers(bs):
    # 获取存放去往followers页面的a标签
    followersLinkATag = bs.findAll('a', attrs={'class': "followers"})
    # 获取a标签里面的链接
    findFollowersLink = re.compile(r'<a.*?href="(.*?)"')
    followersLink = re.findall(findFollowersLink, str(followersLinkATag))[0]
    url = baseUrl + followersLink
    print(url)
    followerBS = isNeedReSendUrl(getHtml(url), url)
    followersNumberTag = followerBS.select('#followers > div.block-title > span')
    findFollowersNumber = re.compile(r'(\d+)')
    followersNumber = re.findall(findFollowersNumber, str(followersNumberTag))[0]
    followersUserNames = []
    # 用来限制拿取的数据多少，全部拿的时候可以删掉
    # k = 0
    while True:
        # k = k + 1
        # if k == 3:
        #     break

        # 找到所有的username
        tbody = followerBS.tbody
        findFollowersUserNames = re.compile(r'<a class="username" .*?>(.*?)</a>')
        followersUserNames2 = re.findall(findFollowersUserNames, str(tbody))
        # print(followersUserNames2)
        for item in followersUserNames2:
            followersUserNames.append(item)

        loadMoreATag = followerBS.select('#pager_id_list_all')
        if len(loadMoreATag) == 0:
            print("没有更多数据了")
            break
        findLoadMoreLink = re.compile(r'<a class=".*?" href="(.*?)"')
        loadMoreLink = re.findall(findLoadMoreLink, str(loadMoreATag))[0]
        # 拿到的连接可能因为特殊字符的问题，多了一个amp; 需要替换掉，不然就回出错
        loadMoreLink = (baseUrl + loadMoreLink).replace("amp;", "")
        print(loadMoreLink)
        followerBS = isNeedReSendUrl(getHtml(loadMoreLink), loadMoreLink)


    # 找到加载更多按钮的链接
    return {'followersNumber': followersNumber, 'followersUserNames': partitionedArray(followersUserNames)}



# 处理APIName值，使其能够拼凑成相应的url地址进行访问
def subApiName(apiName):
    return str(apiName).replace(" ", "-").replace("-API", "").replace(".", "").lower()


