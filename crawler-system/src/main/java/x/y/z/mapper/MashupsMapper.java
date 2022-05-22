package x.y.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import x.y.z.entity.Mashups;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;

import java.util.List;

@Repository
public interface MashupsMapper extends BaseMapper<Mashups> {
    //还有另外一种SQL的写法：
    /**
     * 把这个相关的api截取第一个，然后作为主类别
     * 如zanox###Finansportale截取第一个得到zanox，然后统计这个出现的次数
     * @return
     */
    //SELECT COUNT(*),main_categories FROM (SELECT SUBSTRING_INDEX(categories,'###',1) AS main_categories FROM mashups) AS temp GROUP BY main_categories  ORDER BY COUNT(*) DESC
    @Select("SELECT COUNT(*) AS total,categories FROM (  SELECT CASE WHEN INSTR(categories,'#')>0 THEN LEFT(categories,INSTR(categories,'#')-1)  " +
            "  ELSE categories END categories  FROM mashups) temp  GROUP BY categories  ORDER BY COUNT(*) DESC")
    List<TempResult> groupQuery();

    /**
     * 把这个相关的api字段分隔，然后统计
     * 如zanox###Finansportalen分隔得到zanox和Finansportalen，然后统计这个出现的次数
     * @return
     */
    @Select("SELECT COUNT(*) AS total,SUBSTRING_INDEX(SUBSTRING_INDEX(related_apis,'###',b.help_topic_id + 1),'###' ,- 1) AS related_apis " +
            "FROM mashups JOIN mysql.help_topic b ON b.help_topic_id < ( LENGTH(related_apis) - LENGTH( REPLACE (related_apis, '###', '')) + 1) " +
            "GROUP BY related_apis ORDER BY COUNT(*) DESC ")
    List<TempResult2> selectStatisticsApis();

    /**
     * 把categories字段的第一个主类别去除后，然后获取剩余的剩余的名称分为开，作为tag
     * 如Mail###Data###Marketing，我先先截取得到Data###Marketing，
     * 然后再把截取字符串分隔，得到Data和Marketing，然后统计这些出现的个数
     */
    @Select("SELECT COUNT(*) AS total,SUBSTRING_INDEX(SUBSTRING_INDEX(tag,'###',b.help_topic_id + 1),'###' ,- 1) AS tag2\n" +
            "    FROM  (SELECT  CASE WHEN INSTR(categories,'#')>0 THEN SUBSTR(categories,INSTR(categories,'#')+3) ELSE NULL  END tag ,categories FROM mashups) AS temp\n" +
            "    JOIN mysql.help_topic b ON b.help_topic_id < ( LENGTH(tag) - LENGTH( REPLACE (tag, '###', '')) + 1) \n" +
            "     GROUP BY tag2 ORDER BY total DESC")
    List<TempTagResult> selectTag();
}
