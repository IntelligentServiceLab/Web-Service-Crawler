package x.y.z.service;

import x.y.z.entity.SampleSourceCode;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;

import java.util.List;

public interface SampleSourceCodeService {
    /**
     * 查询所有记录
     * @return
     */
    List<SampleSourceCode> selectAll();
    /**
     * 根据page来查询记录
     */
    List<SampleSourceCode> selectByPage(int page,int size,String sortName);
    /**
     * 获取总记录条数
     * @return
     */
    Long getCount();
    /**
     *得到排名前五的类别
     * @return
     */
    List<TempResult> groupQuery();
    /**
     * 关联最高的五组apis
     * @return
     */
    List<TempResult2> selectStatisticsApis();
    /**
     * 根据名称进行模糊查询
     * @param name
     * @return
     */
    List<SampleSourceCode> searchLikeName(String name,String value,int page,int size,String sortName);
    /**
     * 把categories字段的第一个主类别去除后，然后获取剩余的剩余的名称分为开，作为tag
     * 如Mail###Data###Marketing，我先先截取得到Data###Marketing，
     * 然后再把截取字符串分隔，得到Data和Marketing，然后统计这些出现的个数
     * @return
     */
    List<TempTagResult> selectTag();
}
