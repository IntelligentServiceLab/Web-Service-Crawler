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
      <el-table-column  width="80" align="center" label="序号" type='index'>
      </el-table-column>
      <el-table-column prop="mashupsName" label="MashupName" width='125'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort"
            type='text' @click='sortSearch("mashupsName")'>
            MashupName
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="relatedApis" label="RelatedAPIs" width='115' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("relatedApis")'>
            RelatedAPIs
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="Description" width='110' show-overflow-tooltip >
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("description")'>
            Description
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="categories" label="Categories" width='110' show-overflow-tooltip>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("categories")'>
            Categories
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="submittedDate" label="SubmittedDate" width='125'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("submittedDate")'>
            SubmittedDate
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="mashupAppType" label="Mashup/AppType" width='150'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("mashupAppType")'>
            Mashup/AppType
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="company" label="Company" width='100'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("company")'>
            Company
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followersNumbers" label="FollowersNumbers" width='160'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("followersNumbers")'>
            FollowersNumbers
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="followersNames" label="FollowersNames" width='160'>
        <template slot="header">
          <el-button
            class="el-icon-caret-bottom btn-sort "
            type='text' @click='sortSearch("followersNames")'>
            FollowersNames
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
import mashups from '@/api/core/mashups'
export default {
  name: 'MashupsModule',
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
        value: 'mashupsName',
        label: 'mashupsName'
      }, {
        value: 'relatedApis',
        label: 'relatedApis'
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
        value: 'mashupAppType',
        label: 'mashupAppType'
      }, {
        value: 'company',
        label: 'company'
      },{
        value: 'followersNumbers',
        label: 'followersNumbers'
      },{
        value: 'followersNames',
        label: 'followersNames'
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
      mashups.pageRecords(this.currentPage,this.currentSize,this.sortName).then(response => {
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
        this.fetchData()
      }else{
        this.searchLikeName()
      }
    },
    //导出数据
    exportData(){
      window.location.href = this.BASE_API + "/mashups/export"
    },
    searchLikeName(){
      mashups.searchLikeName(this.selectName,this.searchValue,this.currentPage,this.currentSize,this.sortName).then(response=>{
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
    // removeById(id) {
    //   this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     integralGradeApi.removeById(id).then(response => {
    //       this.$message.success(response.message)
    //       this.fetchData()
    //     })
    //   }).catch(() => {
    //     this.$message.info('取消删除')
    //   })
    // }
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
