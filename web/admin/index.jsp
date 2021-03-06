<%--
  Created by IntelliJ IDEA.
  User: 15695
  Date: 05-27 027
  Time: 下午 9:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="commons/header.jsp"%>
<html>
<head>
    <title>Title</title>
    <style>
        .modal-dialog{
            width: 800px;
        }
    </style>
</head>
<body>
<div class="container-fluid col-xs-push-1 col-lg-10">

    <br><br>
    <!--    查询功能-->
    <form class="form-inline" onsubmit="javascript: return false;">
        <div class="form-group">
            <label for="name">用户名: </label>
            <input autocomplete="off" class="form-control" id="name">
        </div>
        <div class="form-group">
            <label for="gender">性别: </label>
            <select class="form-control" id="gender">
                <option value="-1">全部</option>
                <option value="F">女</option>
                <option value="M">男</option>
            </select>
        </div>
        <div class="form-group">
            <label for="status">状态: </label>
            <select class="form-control" id="status">
                <option value="-1">全部</option>
                <option value="1">激活</option>
                <option value="0">未激活</option>
            </select>
        </div>
        <div class="form-group">
            <label for="email">邮件: </label>
            <input autocomplete="off" class="form-control" id="email">
        </div>
        <div class="form-group">
            <label>注册日期: </label>
            <input class="form-control" readonly autocomplete="off" id="beginRegisterDate"> -
            <input autocomplete="off" readonly class="form-control" id="endRegisterDate">
        </div>
        <div class="form-group">
            <button class="btn btn-danger" onclick="querySearch()">搜索</button>
            <button autocomplete="off" class="btn btn-danger" onclick="resetSearch()">重置</button>
        </div>
    </form>

    <br><br>

    <!--    显示用户信息的table-->
    <table id="dataTable" class="table table-hover table-bordered"></table>

    <!--  模态窗 -->
    <!--    显示用户所有的地址信息-->
    <div class="modal" id="showAddress" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button class="close" onclick="closeModal()">&times;</button>
                    <h3 class="modal-title">用户地址</h3>
                </div>
                <div class="modal-body">

                    <table id="AddressTable" class="table-bordered table table-hover table-striped tab-content"></table>

                </div>
                <div class="modal-footer">
                    <!--                    <button class="btn btn-danger" data-dismiss="modal">关闭</button>-->
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<%@ include file="commons/footer.jsp"%>
<script>
    /**
     *
     "rows": [
     {
          "email": "223@qq.com",
          "id": 1,
          "name": "张三",
          "phone": "13000000000",
          "registerDate": 1589353095000,
          "sex": "F",
          "status": 1
        }
     ],
     "total": 1
     }
     */

    //显示所有数据
    $('#dataTable').bootstrapTable({
        url: 'http://localhost:8080/Demo5_13shopping_war_exploded/user',  //请求的地址
        method: 'GET', //请求的方式
        // columns: 是定义列的信息， title表头信息, field表示从返回的json数据中取对应的属性的
        columns: [
            {field: 'id', title: 'ID', align: 'center'},
            {field: 'name', title: '姓名', align: 'center'},
            // formatter要接收一个函数, 是对数据进行处理，该函数接收四个参数。
            {field: 'sex', title: '性别', align: 'center', formatter: formatSex},
            {field: 'status', title: '状态', align: 'center', formatter: formatStatus},
            {field: 'phone', title: '电话', align: 'center'},
            {field: 'email', title: '邮件', align: 'center'},
            {field: 'registerDate', title: '注册日期', align: 'center', formatter: formatRegisterDate},
            { title: '操作', formatter: addProcessBtns}
        ],
        pagination: true,  //表示意思是要分页, 默认是false
        // 在那边分页，值为 "client", "server", client的意思一次性将数据全部拿到，然后客户端分页。
        // server, 在服务器端分页, 每次查分页的数据的时候到服务器去请求。
        sidePagination: 'server',
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',//设置之后就可以正常传输数据,要不然数据传不到后端
        queryParams:function (params) {
            //将搜索框中的数据传到后台
            params.method = 'getUserPageData';
            params.name = $('#name').val();
            params.gender = $('#gender').val();
            params.status = $('#status').val();
            params.email = $('#email').val();
            params.beginRegisterDate = $('#beginRegisterDate').val();
            params.endRegisterDate = $('#endRegisterDate').val();

            return params;
        }
    })




    /**
     * 最终表格中显示的数据以该函数的返回值为准
     * value是当前格子的值。
     * row是当前行的数据。
     * index是当前数据的索引。
     */
    function formatSex(value, row, index) {
        return 'F' == value ? '女' : '男';
    }

    // 格式化状态
    function formatStatus(value) {
        // 1表示激活状态(要看数据库)
        if(value == 1) {
            return '<span class="label label-success">激活</span>'
        }else {
            return '<span class="label label-danger">未激活</span>'
        }
    }

    //格式化注册日期
    function formatRegisterDate(value) {
        // 使用moment这个库来完成日期的格式化
        return moment(value).format('YYYY-MM-DD HH:mm:ss');
    }

    //添加按钮
    function addProcessBtns(value, row, index) {
        //定义按钮
        var btn = '';
        btn += '<button class="btn btn-info" onclick="showTakeDeliveryAddress('+ row.id +')" >查看地址</button>\n';
        if(row.status != 1){
            btn += '  <button class="btn btn-success" onclick="activeUser(' + row.id + ')" >激活</button>\n';
        }
        return btn;
    }


    //给按钮添加点击弹出模态窗事件,查询地址
    function showTakeDeliveryAddress(id) {
        $('#showAddress').modal("show");
        //用id调用ajax请求获取所有地址就好了
        $('#AddressTable').bootstrapTable({
            url: 'http://localhost:8080/Demo5_13shopping_war_exploded/address',  //请求的地址
            method: 'GET', //请求的方式
            // columns: 是定义列的信息， title表头信息, field表示从返回的json数据中取对应的属性的
            columns: [
                {field: 'name', title: '姓名', align: 'center'},
                // formatter要接收一个函数, 是对数据进行处理，该函数接收四个参数。
                {field: 'phone', title: '电话', align: 'center'},
                {field: 'detail', title: '地址', align: 'center'},
                {field: 'state', title: '默认地址', align: 'center', formatter: function (value) {
                        return value == 0 ? '否' : '<span class="label label-success">默认地址</span>';
                    }}
            ],
            pagination: true,  //表示意思是要分页, 默认是false
            // 在那边分页，值为 "client", "server", client的意思一次性将数据全部拿到，然后客户端分页。
            // server, 在服务器端分页, 每次查分页的数据的时候到服务器去请求。
            //sidePagination: 'server',
            contentType:'application/x-www-form-urlencoded; charset=UTF-8',//设置之后就可以正常传输数据,要不然数据传不到后端
            queryParams: function (params) { //发送数据到后台
                params.id = id;
                return params;
            }
        })
    }

    //关闭模态窗时刷新页面
    function closeModal() {
        $('#showAddress').modal("hide");
        $('#AddressTable').bootstrapTable('destroy');

    }

    //激活用户
    function activeUser(id) {
        $.ajax({
            url:'http://localhost:8080/Demo5_13shopping_war_exploded/user',
            type:'GET',
            datatype: 'json',
            data:{id : id,method : 'activeUser'},
            success : function (_data) {
                console.log(_data)
                if (_data.code == '-1'){//没有激活
                    Lobibox.notify('info', {//弹窗提醒
                        title: '提示',
                        msg: '激活失败, 请联系管理员',
                        size: 'mini',
                        position: 'bottom right',
                        delay: 2000
                    })
                }else {
                    $('#dataTable').bootstrapTable('refresh');
                }

            }
        })
    }

    //搜索框的日历事件
    $('#beginRegisterDate, #endRegisterDate').datetimepicker({
        // 格式化 yyyy-mm-dd hh:ii:ss 年-月-日 时-分-秒
        format: 'yyyy-mm-dd hh:ii:ss'
    })

    //搜索按钮
    function querySearch() {
        //刷新table
        // $('#dataTable').bootstrapTable('refresh');
        $('#dataTable').bootstrapTable('selectPage',1);
    }

    //重置按钮
    function resetSearch() {
        $('#name').val('');
        $('#gender').val('-1');
        $('#status').val('-1');
        $('#email').val('');
        $('#beginRegisterDate').val('');
        $('#endRegisterDate').val('');
        querySearch();
    }



</script>
</html>
