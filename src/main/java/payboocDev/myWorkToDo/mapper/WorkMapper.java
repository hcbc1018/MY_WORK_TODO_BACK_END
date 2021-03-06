package payboocDev.myWorkToDo.mapper;

import org.apache.ibatis.annotations.*;
import payboocDev.myWorkToDo.model.User;
import payboocDev.myWorkToDo.model.Work;

import java.util.List;

@Mapper
public interface WorkMapper {

    //work info
    @Select("SELECT work_id, name, date_format(to_date, '%Y-%m-%d')as to_date FROM WTD_WORK WHERE work_id=#{work_id}")
    Work getWorkInfo(@Param("work_id") int work_id);

    //get recent weekly work list
    @Select("SELECT work_id, name, group_name, user_id, auth, team_name, date_format(created_date, '%Y-%m-%d')as created_date, date_format(to_date, '%Y-%m-%d')as to_date\n" +
            ",(select name from wtd_user where user_id = group_master ) as group_master\n" +
            ",(select count(*) as group_number from wtd_group where group_work_id = wtd_work.work_id) as group_number\n" +
            "FROM wtd_work\n" +
            "WHERE work_id IN (select group_work_id from wtd_group where group_member = #{user_id})\n" +
            "ORDER BY to_date ASC \n"+
            "LIMIT 5;")
    List<Work> getRecentWorkList(@Param("user_id") int user_id);


    //get all my work list (for work detail link)
    @Select("SELECT work_id as id, name, group_name, user_id, auth, group_number, team_name, date_format(created_date, '%Y-%m-%d')as created_date, date_format(to_date, '%Y-%m-%d')as to_date\n" +
            ",(select name from wtd_user where user_id = group_master ) as group_master\n" +
            ",(select count(*) as group_number from wtd_group where group_work_id = wtd_work.work_id) as group_number\n" +
            "FROM wtd_work\n" +
            "WHERE work_id IN (select group_work_id from wtd_group where group_member = #{user_id})\n"+
            "ORDER BY to_date ASC")
    List<Work> getAllMyWorkList(@Param("user_id") int user_id);

    //insert work
    @Insert("INSERT INTO WTD_WORK(work_id,name,group_name,user_id,auth,group_number,group_master,team_name, created_date,to_date ) VALUES(get_seq('work_master_sequence'), #{name}, #{group_name}, #{user_id},#{auth},#{group_number},#{group_master},#{team_name},NOW(), #{to_date})")
    int insertWork(@Param("name") String name, @Param("group_name") String group_name, @Param("user_id") int user_id, @Param("auth") String auth, @Param("group_number") int group_number, @Param("group_master") String group_master, @Param("team_name") String team_name, @Param("to_date") String to_date);

    @Update("UPDATE WTD_WORK SET name=#{name}, email=#{email}, address=#{address} WHERE id=#{id}")
    int updateWork(@Param("id") int id, @Param("name") String name, @Param("email") String email, @Param("address") String address);

    //delete work
    @Delete("DELETE FROM WTD_WORK WHERE work_id=#{work_id}")
    int deleteWork(@Param("work_id") int work_id);



}
