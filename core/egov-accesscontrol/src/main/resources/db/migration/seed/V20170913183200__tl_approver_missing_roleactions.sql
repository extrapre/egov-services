insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='UpdateLegacyLicense'),'default');
