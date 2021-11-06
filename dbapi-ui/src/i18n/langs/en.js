import enLocale from 'element-ui/lib/locale/lang/en' // 引入element语言包
const en = {
    m: {
        login: 'Login',
        cancel:'Cancel',
        export:'Export',
        datasource: 'Datasource',
        authority: 'Authority',
        settings: 'Settings',
        createDS: 'New Datasource',
        exportDS: 'Export Datasources',
        importDS: 'Import Datasources',
        database: 'Database',
        name: 'Name',
        note: 'Note',
        jdbc_driver_class: 'JDBC Driver Class',
        username: 'Username',
        password: 'Password',
        sql_query_all_table_name: 'SQL that get table names',
        test_connection: 'Test Connection',
        save: 'Save',
        create_ds: 'New Datasource',
        update_ds: 'Modify Datasource',
        export_ds: 'Export Datasource',
        import_ds: 'Import Datasource',
        update_time: 'Update Time',
        operation: 'Operation',
        back: 'Back',
        ds_sql_tip: 'When you create or edit API, you should click to choose datasource, then this sql will be executed to query names of all tables',
        ds_driver_tip: 'If you choose other db,please make sure the corresponding jdbc driver jar exists.If not, you need to copy the jar file to lib/ directory.',

        create_api:'New API',
        create_group:'New Group',
        api_group_manage:'API Group Manage',
        export_api_doc: 'Export API Doc',
        export_api: 'Export API',
        import_api: 'Import API',
        export_api_groups:'Export API Groups',
        import_api_groups:'Import API Groups',
        api_group:'API Group',
        search:'Search',
        input_keyword:'Please input keyword',
        path:'Path',
        parameters:'Parameters',
        access:'Access',
        private:'Private',
        public:'Public',
        data_convert:'Data Convert',
        cache:'Cache',
        basic_info:'Basic Info',
        access_tip:'Public API can be accessed directly.Private API can only be accessed with token.Learn more from the Authority menu'



    },
    ...enLocale
}

export default en