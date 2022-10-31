//마이페이지 포스트
function mypagePopup(itemId,itemtitle,itemDetail,itemUrl,obj) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(obj).css("display", "flex");

    $('#btnpage').hide();

    var mainItemDetailDto = {};
    mainItemDetailDto.id = itemId;
    mainItemDetailDto.title = itemtitle;
    mainItemDetailDto.itemDetail = itemDetail;
    mainItemDetailDto.imgUrl = itemUrl;
    
    $.ajax({
        url      : "/user/item/detail/"+itemId,
        type     : "GET",
        data     : mainItemDetailDto,
        beforeSend : function(xhr){
            //  데이터를 전송하기 전에 헤더에 csrf값을 설정 
            xhr.setRequestHeader(header, token);
        },
        cache   : false,
        success  : function(result, status){
           let item = getmypagePostModalInfo(result);
            $("#postInfoModal").html(item);
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

function ItemDelete(MainItemDetailDtoId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    Swal.fire({
        title: '정말 삭제 하시겠습니까?',
        // text: "다시 되돌릴 수 없습니다. 신중하세요.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then((result) => {
        
        if (result.isConfirmed) {
            $.ajax({
                url      : "/user/item/delete/"+MainItemDetailDtoId,
                type     : "DELETE",
                data     : MainItemDetailDtoId,
                beforeSend : function(xhr){
                    //  데이터를 전송하기 전에 헤더에 csrf값을 설정 
                    xhr.setRequestHeader(header, token);
                },
                cache   : false,
                success  : function(result, status){
                    Swal.fire(
                        '삭제가 완료되었습니다.',
                        '',
                        'success'
                    ).then((result)=>{
                        if(result.isConfirmed){
                            location.href="/user/items"
                        }
                    })
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

    })

}

function getmypagePostModalInfo(MainItemDetailDto) {
    let item = `
    <div class="subscribe-header">
            <span>${MainItemDetailDto.title}</span> `;
            item += `<button class="exit" onclick="modalClose()"><i class="fas fa-times"></i></button>`
            if(MainItemDetailDto.register) {
                item += `<button class="edit" onclick="location.href='/user/item/${MainItemDetailDto.id}'"><i class="far fa-edit"></i></button>
                <button class="delete" onclick="ItemDelete(${MainItemDetailDto.id})"><i class="far fa-trash-alt"></i></button>`
            }
    item += `
    </div>
    <div class="post-box">
	    <div class="subscribe__img">
		    <img src="${MainItemDetailDto.imgUrl}"/>
	    </div>

        <div class="text">
	            <span>${MainItemDetailDto.itemDetail}</span>
        </div>
    
    </div>`;
    return item;
}

//마이페이지화면 사진 자세히보기 후 X 클릭시
function modalClose(MainItemDetailDto) {
    $(".modal-post").css("display", "none");
    $('#btnpage').show();
}