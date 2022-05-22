<template>
  <div class="app-container">
    <!-- 表格 -->
    <el-button @click='exportData' icon='el-icon-upload2' type='primary' >导出所有数据</el-button>
    <el-button class='btn-search' @click='btnSearchLikeName()'>搜索</el-button>
    <el-input class='search'
              :placeholder="'按照'+this.selectName+'搜索'"
              v-model="searchValue"
              @change='btnSearchLikeName()'
              clearable>
    </el-input>
    <el-select class='select' v-model="selectName" placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <el-table class='mashupsTable' :data="list" border stripe :cell-style="{textAlign:'center'}">
      <el-table-column width="80" align="center" label="序号" type='index'>
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ (currentPage-1)*currentSize+(scope.$index+1) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sampleSourceCodeName" label="sampleSourceCodeName" width='190' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("sampleSourceCodeName")'>
            sampleSourceCodeName
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="description" width='110' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("description")'>
            description
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="relatedApis" label="relatedApis" width='120' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("relatedApis")'>
            relatedApis
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="relatedPlatformLanguages" label="relatedPlatformLanguages" width='200' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("relatedPlatformLanguages")'>
            relatedPlatformLanguages
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="categories" label="categories" width='110' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("categories")'>
            categories
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="addedDate" label="addedDate" width='105' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("addedDate")'>
            addedDate
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="versions" label="versions" width='90' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("versions")'>
            versions
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="sampleSourceCodeProvider" label="sampleSourceCodeProvider" width='210' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("sampleSourceCodeProvider")'>
            sampleSourceCodeProvider
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followersNumber" label="followersNumber" width='150' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("followersNumber")'>
            followersNumber
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followers" label="followers" width='100' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("followers")'>
            followers
          </el-button>
        </template>
      </el-table-column>



    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="pageSizes"
      :page-size='currentSize'
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

  </div>
</template>

<script>
  import sample from '@/api/core/sample'
  import apisData from '@/api/core/apisData'
  export default {
    name: 'List',
    data() {
      return {
        searchValue: "",
        list: [],
        total: 0,
        //显示页面记录条数
        pageSizes: [5, 10, 20, 30],
        //当亲页面显示的记录条数
        currentSize: 5,
        //当前页码
        currentPage: 1,
        BASE_API: process.env.VUE_APP_BASE_API,
        selectName: '',
        options: [{
          value: 'sampleSourceCodeName',
          label: 'sampleSourceCodeName'
        }, {
          value: 'description',
          label: 'description'
        }, {
          value: 'relatedApis',
          label: 'relatedApis'
        },{
          value: 'relatedPlatformLanguages',
          label: 'relatedPlatformLanguages'
        },{
          value: 'categories',
          label: 'categories'
        }, {
          value: 'addedDate',
          label: 'addedDate'
        },{
          value: 'versions',
          label: 'versions'
        },{
          value: 'sampleSourceCodeProvider',
          label: 'sampleSourceCodeProvider'
        },{
          value: 'followersNumber',
          label: 'followersNumber'
        },{
          value: 'followers',
          label: 'followers'
        }],
        //按照哪个字段排序
        sortName: null
      }
    },
    // 页面渲染成功后获取数据
    created() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        sample.pageRecords(this.currentPage,this.currentSize,this.sortName).then(response => {
          this.total = response.data.slice(-1)[0].total
          this.list  = response.data.slice(0,-1)
        })
      },
      handleSizeChange(val) {
        this.currentSize = val
        if(this.searchValue==""){
          this.fetchData()
        }else{
          this.searchLikeName()
        }
      },
      handleCurrentChange(val) {
        this.currentPage = val
        if(this.searchValue==""){
          this.fetchData(e)
        }else{
          this.searchLikeName()
        }
      },
      //导出数据
      exportData(){
        window.location.href = this.BASE_API + "/sampleSourceCode/export"
      },
      searchLikeName(){
        sample.searchLikeName(this.selectName,this.searchValue,this.currentPage,this.currentSize,this.sortName).then(response=>{
          this.total = response.data.slice(-1)[0].total
          this.list  = response.data.slice(0,-1)
          console.log("搜索的结果数据",this.list)
        })
      },
      btnSearchLikeName(){
        //为了清除之前的排序
        this.sortName = null
        //如果之前在第后面的页数，搜索新的数据也会保留在后面的页数，有时候有可能没有数据
        this.currentPage = 1
        this.searchLikeName()
      },
      sortSearch(sortName){
        console.log(sortName+'排序')
        this.sortName = sortName;
        if(this.searchValue==""){
          this.fetchData()
        }else{
          this.searchLikeName()
        }
      }

    }
  }
</script>

<style lang='scss'>
  .search{
    width: 30%;
    float: right;
  }
  .btn-search{
    float: right;
  }
  .select{
    float: right;
  }
  .mashupsTable{
    el-table-column{
      text-align: center;
    }
    .btn-sort{
      color: #909399;
      font-size: 10px;
      font-weight: bold;
    }
  }
</style>
