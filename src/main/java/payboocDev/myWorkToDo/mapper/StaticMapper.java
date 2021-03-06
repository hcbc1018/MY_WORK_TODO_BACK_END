package payboocDev.myWorkToDo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import payboocDev.myWorkToDo.model.Task;

import java.util.List;

@Mapper
public interface StaticMapper {

    @Select("SELECT sum(if(step=\"처리중\",1,0)) as working_number, sum(if(step=\"완료\",1,0)) as complete_number,\n" +
            "sum(if(step=\"해야할일\",1,0)) as todo_number, sum(if(step=\"보류\",1,0)) as stop_number\n" +
            "FROM wtd_task\n" +
            "where user_id = #{user_id}")
    Task getTaskSummary(@Param("user_id") int user_id);

    @Select("SELECT ((count(task_id) / (select count(task_id) from wtd_task) ) * 100 ) as myTaskRatio,\n" +
            " ( (select count(task_id) from wtd_task where step = '완료') / (select count(task_id) from wtd_task) * 100 ) as allTaskRatio\n" +
            "FROM wtd_task \n" +
            "WHERE user_id = #{user_id} \n" +
            "and date_format(task_to_date, '%d') >= (date_format(now(), '%d') -4)\n" +
            "and date_format(task_to_date, '%d') <= (date_format(now(), '%d') +4)")
    Task getTaskTotalSummary(@Param("user_id") int user_id);

    @Select(
            "select (select name from wtd_user where user_id = wtd_task.user_id limit 1) as name,\n" +
            "count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') -4 ) then 1 end) as daypre4 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') -3 ) then 1 end) as daypre3 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') -2 ) then 1 end) as daypre2 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') -1 ) then 1 end) as daypre1 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d')  ) then 1 end) as daytoday \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') +1 ) then 1 end) as daypost1 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') +2 ) then 1 end) as daypost2 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') +3 ) then 1 end) as daypost3 \n" +
            ",count(case when date_format(task_to_date, '%d') >= (date_format(now(), '%d') +4 ) then 1 end)as daypost4 \n" +
            "from wtd_task\n" + "group by user_id")
    List<Task> getAllTaskStaticList(@Param("user_id") int user_id);
}
