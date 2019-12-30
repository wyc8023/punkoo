<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--添加相关样式引用-->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.bootcss.com/bootstrap-table/1.15.4/bootstrap-table.min.css" rel="stylesheet"/>
<link href="/inmobi_demo/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<div class="container body-content" style="padding-top:20px;">
    <div class="panel panel-default">
        <div class="panel-heading">
            查询条件
        </div>
        <div class="panel-body">
            <form class="form-inline">
                <div class="row">
                    <div class="col-sm-4">
                        <label class="control-label">campaign id：</label><input id="campaignId" type="text" class="form-control">
                    </div>
                    <div class="col-sm-4">
                        <label class="control-label">site name：</label><input id="siteName" type="text" class="form-control">
                    </div>
                    <div class="col-sm-4">
                        <label class="control-label">adgroupId：</label><input id="adgroupId" type="text" class="form-control">
                    </div>
                </div>
                <div class="row text-right" style="margin-top:20px;">
                    <div class="col-sm-12">
                        <input class="btn btn-primary" type="button" value="select" onclick="SearchData()"><input class="btn btn-default" type="button" value="BtchDelete" onclick="BtchDeleteData()">
                        <input id="input-ke-2" name="file" type="file"  class="file" multiple data-min-file-count="1" >
                        <input class="btn btn-primary" type="button" value="export" onclick="exportData()">
                    </div>
                </div>
            </form>
        </div>
    </div>
    <table id="table">
    </table>
</div>
<!--添加相关脚本引用-->
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="/inmobi_demo/js/fileinput.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-table/1.15.4/bootstrap-table.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-table/1.15.4/extensions/editable/bootstrap-table-editable.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-table/1.15.4/locale/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#table').bootstrapTable({
            url: "/inmobi_demo/inmobi/demo/log/select",
            queryParamsType: '',              //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort//
            queryParams: queryParams,
            method: "get",
            pagination: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 50, 100],
            sidePagination: "server",         //分页方式：client客户端分页，server服务端分页（*）
            striped: true,                    //是否显示行间隔色
            cache: false,
            uniqueId: "logId",               //每一行的唯一标识，一般为主键列
            height:500,
            paginationPreText: "上一页",
            paginationNextText: "下一页",
            responseHandler: responseHandler,
            onDblClickCell : function(field,value,row,$element){
                var upIndex = $element[0].parentElement.rowIndex -1;
                    $element[0].innerHTML = "<input id='inputCell' type='textarea' name='inputCell' style ='width: 200px;height: 100px' value='" + value + "'>";
                    $("#inputCell").focus();
                    $("#inputCell").blur(function () {
                        var newValue = $("#inputCell").val();
                        row[field] = newValue;
                        $(this).remove();
                        $('#table').bootstrapTable('updateCell', {
                            index: upIndex,
                            field: field,
                            value: newValue
                        });
                        updateSave(row);
                    });
                },
            columns: [
                { checkbox: true },
                { title: 'index', align: "center", formatter: function (value, row, index) { return index + 1; }  },
                { title: 'campaign id', field: 'campaignId' },
                { title: 'campaign name', field: 'campaignName' },
                { title: 'adgroup id', field: 'adgroupId' },
                { title: 'adgroup name', field: 'adgroupName' },
                { title: 'site id', field: 'siteId' },
                { title: 'site name', field: 'siteName' },
                { title: 'request uid', field: 'requestUid' },
                { title: 'ad impressions rendered', field: 'adImpressionsRendered' },
                { title: 'date', field: 'date', formatter: function (value, row, index) {
                        if (value == null)
                            return "";
                    return timestampToDate(value)
                    }
                },
                { title: 'createTime', field: 'createTime', formatter: function (value, row, index) {
                        if (value == null)
                            return "";
                        return timestampToTime(value)
                    }
                },
                { title: 'updateTime', field: 'updateTime', formatter: function (value, row, index) {
                        if (value == null)
                            return "";
                        return timestampToTime(value)
                    }
                }]
        });
    });

    function responseHandler(result) {
        var temp = {
            // 下面这两个参数是必须有的, 名称不能变
            // 总的数量
            total : result.data.resultCount,
            // 数据
            rows : result.data.resultList
        };

        return temp;
    }

    //查询条件
    function queryParams(params) {
        return {
            perpage: params.pageSize,
            page: params.pageNumber,
            campaignId: $.trim($("#campaignId").val()),
            siteName: $.trim($("#siteName").val()),
            adgroupId: $.trim($("#adgroupId").val()),
        };
    }

    //查询事件
    function SearchData() {
        $('#table').bootstrapTable('refresh', { pageNumber: 1 });
    }


    //批量删除
    function BtchDeleteData(){
        var opts = $('#table').bootstrapTable('getSelections');
        if (opts == "") {
            alert("请选择要删除的数据");
        } else {
            var idArray = [];
            for (var i = 0; i < opts.length; i++) {
                idArray.push(opts[i].logId);
            }
            if (confirm("确定删除logId：" + idArray + "吗？")) {
                deleteLog(idArray)
            }
        }
    }


    function timestampToDate(timestamp) {
        var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        return Y+M+D;
    }
    function timestampToTime(timestamp) {
        var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes() + ':';
        s = date.getSeconds();
        return Y+M+D+h+m+s;
    }

    function updateSave(row) {
        $.ajax({
            url:"/inmobi_demo/inmobi/demo/log/update",
            type:"POST",
            dataType : "json",
            data:JSON.stringify(row),
            contentType:"application/json",
            success:function (res) {
                alert("操作成功");
            },
            error:function (err) {
                alert("操作失败请联系管理员");
            }
        })
    }

    function deleteLog(logIds) {
        $.ajax({
            url:"/inmobi_demo/inmobi/demo/log/delete",
            type:"post",
            dataType : "json",
            contentType:"application/json",
            data:JSON.stringify(logIds),
            success:function (res) {
                alert("删除成功");
                $('#table').bootstrapTable('refresh');
            },
            error:function (err) {
                alert("删除失败请联系管理员");

            }
        })
    }

    $("#input-ke-2").fileinput({

        language: 'zh', //设置语言

        uploadUrl:"/inmobi_demo/inmobi/demo/log/import", //上传的地址

        allowedFileExtensions: ['csv'],//接收的文件后缀

        //uploadExtraData:{"id": 1, "fileName":'123.mp3'},

        uploadAsync: true, //默认异步上传

        showUpload:true, //是否显示上传按钮

        showRemove :true, //显示移除按钮

        showPreview :true, //是否显示预览

        showCaption:false,//是否显示标题

        browseClass:"btn btn-primary", //按钮样式    

        dropZoneEnabled: false,//是否显示拖拽区域

        //minImageWidth: 50, //图片的最小宽度

        //minImageHeight: 50,//图片的最小高度

        //maxImageWidth: 1000,//图片的最大宽度

        //maxImageHeight: 1000,//图片的最大高度

        //maxFileSize:0,//单位为kb，如果为0表示不限制文件大小

        //minFileCount: 0,

        maxFileCount:1, //表示允许同时上传的最大文件个数

        enctype:'multipart/form-data',

        validateInitialCount:true,

        previewFileIcon: "<iclass='glyphicon glyphicon-king'></i>",

        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",

    }).on("fileuploaded", function (event, data, previewId, index){
        if(data.response.code==200){
            alert("上传成功")
            location.reload()
        }
    });

    function exportData() {
        var opts = $('#table').bootstrapTable('getSelections');
        var param=""
        if (opts != "") {
            var idArray = [];
            for (var i = 0; i < opts.length; i++) {
                idArray.push(opts[i].logId);
            }
            param="exportLogId="+idArray.join(",")
        }
        var  campaignId=$.trim($("#campaignId").val())
        if (campaignId!=null && campaignId!=''){

            param=param+(param.length>0?"&":"")+"campaignId="+campaignId
        }
        var  siteName=$.trim($("#siteName").val())
        if (siteName!=null && siteName!=''){
            param=param+(param.length>0?"&":"")+"siteName="+siteName
        }
        var  adgroupId=$.trim($("#adgroupId").val())
        if (adgroupId!=null && adgroupId!=''){
            param=param+(param.length>0?"&":"")+"adgroupId="+adgroupId
        }
        window.location="/inmobi_demo/inmobi/demo/log/export"+(param.length>0?("?"+param):"");
    }
</script>
