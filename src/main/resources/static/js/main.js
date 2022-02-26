//메인화면 사진 클릭시
function mainPopup(itemId,itemtitle,itemDetail,imgUrl,obj) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(obj).css("display", "flex");

    $('#btnpage').hide();

    var mainItemDetailDto = {};
    mainItemDetailDto.id = itemId;
    mainItemDetailDto.title = itemtitle;
    mainItemDetailDto.itemDetail = itemDetail;
    mainItemDetailDto.imgUrl = imgUrl;

    $.ajax({
        url      : "/api/item",
        type     : "GET",
        data     : mainItemDetailDto,
        beforeSend : function(xhr){
            //  데이터를 전송하기 전에 헤더에 csrf값을 설정 
            xhr.setRequestHeader(header, token);
        },
        cache   : false,
        success  : function(result, status){
           let item = getPostModalInfo(result);
            //$("#postInfoModal").append(item); 사용하면 처음에 클릭했던 게시물 값만 가져옴
            $("#postInfoModal").html(item) //클릭한 게시물들을 각각 가져옴
        },
        error : function(jqXHR, status, error){

            if(jqXHR.status == '401'){
                // location.href='/members/login';
            } else{
                // alert(jqXHR.responseText);
            }

        }
    });

}

//메인화면 사진 자세히보기
function getPostModalInfo(MainItemDetailDto) {
    let item = `
    <div class="subscribe-header">
            <span>찜?</span> `;
            item += `<button class="exit" onclick="modalClose()"><i class="fas fa-times"></i></button>`
            // if(MainItemDetailDto.register) {
            //     item += `<button class="edit" onclick="location.href='/user/item/${MainItemDetailDto.id}'"><i class="far fa-edit"></i></button>`
            // }
    item += `
    </div>
    <div class="post-box">
	    <div class="subscribe__img">
		    <img src="${MainItemDetailDto.imgUrl}"/>
	    </div>

        <div class="post-info">
            <div class="text">
	            <span>${MainItemDetailDto.title}</span>
            </div>
        </div>

        <div class="post-info">
            <div class="text">
	            <span>${MainItemDetailDto.itemDetail}</span>
            </div>
        </div>
    </div>`;
    return item;
}

//메인화면 사진 자세히보기 후 X 클릭시
function modalClose(MainItemDetailDto) {
    $(".modal-post").css("display", "none");
    $('#btnpage').show();
}