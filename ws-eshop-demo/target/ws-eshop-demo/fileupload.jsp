<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>
<body>
<h2>后端代码测试</h2>



<h3>springmvc上传文件</h3>
<form name="form1" action="manage/product/upload.do" method="post" enctype="multipart/form-data">
<input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>


富文本图片上传文件
<form name="form2" action="manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
   <input type="file" name="upload_file" />
    <input type="submit" value="富文本图片上传文件" />
</form>

</body>
</html>
