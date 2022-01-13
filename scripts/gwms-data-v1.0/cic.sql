        insert into bas_code_class (created_time, creator_id, domain_id, lock_version, code, name)
        values (now(),1,10000,0,'ABC_SORT_TYPE','Cost item type');
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'en_US' from bas_code_class where code = 'ABC_SORT_TYPE';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'en_TH' from bas_code_class where code = 'ABC_SORT_TYPE';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'th_TH' from bas_code_class where code = 'ABC_SORT_TYPE';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,'ABC排序方式','zh_CN' from bas_code_class where code = 'ABC_SORT_TYPE';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'IQ','IQ',id from  bas_code_class where code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IQ' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IQ' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IQ' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'IQ','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IQ' and cc.code='ABC_SORT_TYPE';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'IK','IK ',id from  bas_code_class where code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IK' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IK' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IK' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'IK','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IK' and cc.code='ABC_SORT_TYPE';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'IV','IV ',id from  bas_code_class where code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IV' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IV' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IV' and cc.code='ABC_SORT_TYPE';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'IV','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='IV' and cc.code='ABC_SORT_TYPE';



        insert into bas_code_class (created_time, creator_id, domain_id, lock_version, code, name) values (now(),1,10000,0,'ABC_ANALYZE_METHOD','Cost item type');
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'en_US' from bas_code_class where code = 'ABC_ANALYZE_METHOD';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'en_TH' from bas_code_class where code = 'ABC_ANALYZE_METHOD';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,name,'th_TH' from bas_code_class where code = 'ABC_ANALYZE_METHOD';
        insert into bas_code_class_i18n(created_time,  creator_id, domain_id,  lock_version,  class_id,  class_code,  class_name,  lang)
        select now(),1,10000,0,id,`code`,'ABC分析方式','zh_CN' from bas_code_class where code = 'ABC_ANALYZE_METHOD';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'OB_QTY','OB_QTY',id from  bas_code_class where code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='OB_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='OB_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='OB_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'OB_QTY','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='OB_QTY' and cc.code='ABC_ANALYZE_METHOD';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'SKU_QTY','SKU_QTY ',id from  bas_code_class where code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_QTY' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'SKU_QTY','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_QTY' and cc.code='ABC_ANALYZE_METHOD';


        insert into  bas_code_info(created_time,  creator_id,  domain_id,  lock_version, code,name,  code_class_id)
        select now(),1,10000,0,'SKU_RANK','SKU_RANK ',id from  bas_code_class where code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_US' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_RANK' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'en_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_RANK' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,c.name,'th_TH' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_RANK' and cc.code='ABC_ANALYZE_METHOD';
        insert into  bas_code_info_i18n( created_time,  creator_id,  domain_id,  lock_version,   info_id,  info_code,  info_name,  lang)
        select now(),1,10000,0,c.id,c.code,'SKU_RANK','zh_CN' from  bas_code_info c , bas_code_class cc where c.code_class_id =cc.id and c.code='SKU_RANK' and cc.code='ABC_ANALYZE_METHOD';
