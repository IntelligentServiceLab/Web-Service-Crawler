package x.y.z.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import x.y.z.entity.Login;
import x.y.z.entity.temp.TempResult;

import java.util.List;

@Repository
public interface LoginMapper extends BaseMapper<Login> {
    @Select("SELECT COUNT(*) AS total,categories FROM (  SELECT CASE WHEN INSTR(categories,'#')>0 THEN LEFT(categories,INSTR(categories,'#')-1)  " +
            "  ELSE categories END categories  FROM apis) temp  GROUP BY categories  ORDER BY COUNT(*) DESC")
    List<TempResult> groupQuery();
}
