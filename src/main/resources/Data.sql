insert into app_role (pk_app_role, role_name)
select seq_app_role.nextval, 'ADMIN' from dual
where not exists (select 1 from app_role where role_name = 'ADMIN');