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
      <el-table-column prop="apiName" label="apiName" show-overflow-tooltip width='100'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("apiName")'>
            apiName
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
      <el-table-column prop="categories" label="categories" width='110' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("categories")'>
            categories
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="submittedDate" label="submittedDate" show-overflow-tooltip width='130' >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("submittedDate")'>
            submittedDate
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="developersNumbers" label="developersNumbers" show-overflow-tooltip width='170' >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("developersNumbers")'>
            developersNumbers
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="developersName" label="developersName" show-overflow-tooltip width='170'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("developersName")'>
            developersNumbers
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="developersMR" label="developersMR" show-overflow-tooltip width='130'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("developersMR")'>
            developersMR
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followersNumbers" label="followersNumbers" show-overflow-tooltip width='160' >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("followersNumbers")'>
            followersNumbers
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followersName" label="followersName" show-overflow-tooltip width='140' >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("followersName")'>
            followersName
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="versions" label="versions" show-overflow-tooltip width='100' >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("versions")'>
            versions
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
        value: 'apiName',
        label: 'apiName'
      }, {
        value: 'description',
        label: 'description'
      }, {
        value: 'categories',
        label: 'categories'
      },{
        value: 'submittedDate',
        label: 'submittedDate'
      },{
        value: 'developersNumbers',
        label: 'developersNumbers'
      }, {
        value: 'developersName',
        label: 'developersName'
      },{
        value: 'developersMR',
        label: 'developersMR'
      },{
        value: 'followersNumbers',
        label: 'followersNumbers'
      },{
        value: 'followersName',
        label: 'followersName'
      },{
        value: 'versions',
        label: 'versions'
      }],
      //按照哪个字段排序
      sortName: null
    }
  },
  computed:{
  },
  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      apisData.pageRecords(this.currentPage,this.currentSize,this.sortName).then(response => {
        this.total = response.data.slice(-1)[0].total
        this.list  = response.data.slice(0,-1)
        console.log(this.list)
      })
    },
    handleSizeChange(val) {
      this.currentSize = val
      if(this.searchName==""){
        this.fetchData()
      }else{
        this.searchLikeName()
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      if(this.searchName==""){
        this.fetchData()
      }else{
        this.searchLikeName()
      }
    },
    //导出数据
    exportData(){
      window.location.href = this.BASE_API + "/apis/export"
    },
    searchLikeName(){
      apisData.searchLikeName(this.selectName,this.searchValue,this.currentPage,this.currentSize,this.sortName).then(response=>{
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
      width: 200px;
    }
    .btn-sort{
      color: #909399;
      font-size: 10px;
      font-weight: bold;
    }
  }
</style>
