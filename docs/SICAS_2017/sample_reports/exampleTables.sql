set echo on
/** Sample tables for the SICAS presentation */

set sqlformat ansiconsole

drop table summit_sql;

create table summit_sql as
    select '1' id, 'select user from dual' sql_query from dual
    union select '2' id, 'select sloozle from fleem' sql_query from dual;

select * from summit_sql;

------------------------------------------------------------------------

drop table summit_grades;

create table summit_grades (
    student_id  number(3),
    semester    varchar2(6),
    mid_grade   varchar2(1),
    final_grade varchar2(1),
    constraint pk_summit_grades
        primary key (student_id, semester)
)
/


DECLARE
    procedure ins(student_id  number,
                  semester    varchar2,
                  mid_grade   varchar2,
                  final_grade varchar2) is
    BEGIN
        insert into summit_grades(
                  student_id,
                  semester,
                  mid_grade,
                  final_grade
               ) values (
                  student_id,
                  semester,
                  mid_grade,
                  final_grade);
        commit;
    END ins;
BEGIN
    ins(12, '201612', 'A', 'A');
    ins(12, '201705', 'B', 'A');
    ins(13, '201612', 'C', 'B');
    ins(13, '201705', 'C', 'A');
    ins(14, '201612', 'F', 'A');
    ins(15, '201712', 'F', 'D');
END;
/
select * from summit_grades;
