package x.y.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import x.y.z.entity.SampleSourceCode;
import x.y.z.entity.temp.TempResult;
import x.y.z.entity.temp.TempResult2;
import x.y.z.entity.temp.TempTagResult;

import java.util.List;

@Repository
public interface SampleSourceCodeMapper extends BaseMapper<SampleSourceCode> {
    @Select("SELECT COUNT(*) AS total,categories FROM (  SELECT CASE WHEN INSTR(categories,'#')>0 THEN LEFT(categories,INSTR(categories,'#')-1)  " +
            "  ELSE categories END categories  FROM sample_source_code) temp  GROUP BY categories  ORDER BY COUNT(*) DESC")
    List<TempResult> groupQuery();
    @Select("SELECT\n" +
            "\tCOUNT(*) AS total,\n" +
            "\tSUBSTRING_INDEX(\n" +
            "\t\tSUBSTRING_INDEX(\n" +
            "\t\t\trelated_apis,\n" +
            "\t\t\t'###',\n" +
            "\t\t\tb.help_topic_id + 1\n" +
            "\t\t),\n" +
            "\t\t'###' ,- 1\n" +
            "\t) AS related_apis\n" +
            "FROM\n" +
            "\tsample_source_code\n" +
            "JOIN mysql.help_topic b ON b.help_topic_id < (\n" +
            "\tLENGTH(related_apis) - LENGTH(\n" +
            "\t\tREPLACE (related_apis, '###', '')\n" +
            "\t) + 1\n" +
            ")\n" +
            "GROUP BY related_apis\n" +
            "ORDER BY COUNT(*) DESC")
    List<TempResult2> selectStatisticsApis();
    /**
     * 把categories字段的第一个主类别去除后，然后获取剩余的剩余的名称分为开，作为tag
     * 如Mail###Data###Marketing，我先先截取得到Data###Marketing，
     * 然后再把截取字符串分隔，得到Data和Marketing，然后统计这些出现的个数
     */
    @Select("SELECT COUNT(*) AS total,SUBSTRING_INDEX(SUBSTRING_INDEX(tag,'###',b.help_topic_id + 1),'###' ,- 1) AS tag2\n" +
            "    FROM  (SELECT  CASE WHEN INSTR(categories,'#')>0 THEN SUBSTR(categories,INSTR(categories,'#')+3) ELSE NULL  END tag ,categories FROM sample_source_code) AS temp\n" +
            "    JOIN mysql.help_topic b ON b.help_topic_id < ( LENGTH(tag) - LENGTH( REPLACE (tag, '###', '')) + 1) \n" +
            "     GROUP BY tag2 ORDER BY total DESC")
    List<TempTagResult> selectTag();
}
