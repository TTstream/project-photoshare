# SpringBoot Project PhotoShare
> springboot + thymeleaf를 활용한 사진 공유 사이트

## 목차
- [1. 프로젝트 소개](#%EF%B8%8F-1-프로젝트-소개)
- [2. 개발 기간](#-2-개발-기간)
  - [2-1. 개발 인원](#-2-1-개발-인원)
  - [2-2. 개발 환경](#%EF%B8%8F-2-2-개발-환경)
- [3. 요구사항 정의](#%EF%B8%8F-3-요구사항-정의)
- [4. DB 설계](#blue_book-4-db-설계)
- [5. API 설계](#green_book-5-api-설계)
- [6. 주요 기능](#-6-주요-기능)
  - [6-1. 로그인 전 사용가능한 기능](#large_blue_circle-6-1-로그인-전-사용가능한-기능)
    - [6-1-1. 페이지 및 검색 기능](#6-1-1-페이지-및-검색-기능)
    - [6-1-2. 게시물 자세히보기 기능](#6-1-2-게시물-자세히보기-기능)
    - [6-1-3. 구글 로그인 기능](#6-1-3-구글-로그인-기능)
  - [6-2. 로그인 후 사용가능한 기능](#large_blue_circle-6-2-로그인-후-사용가능한-기능)
    - [6-2-1. 사진 업로드 기능](#6-2-1-사진-업로드-기능)
    - [6-2-2. 사진관리 기능](#6-2-2-사진관리-기능)
    - [6-2-3. 게시물 수정 및 삭제 기능](#6-2-3-게시물-수정-및-삭제-기능)
- [7. 참고자료](#books-7-참고자료)

## 🖥️ 1. 프로젝트 소개
- 무료 이미지 사이트들을 참고하여 만든 사진 공유 사이트입니다.
- 웹의 기본적인 기능인 CRUD 기능을 구현하고, 그 외의 기능들을 추가적으로 구현하고 있습니다.

## ⌛ 2. 개발 기간
* 2022.01.01 ~ 2022.02.28 / 2022.10.24 ~ 진행중

### 🧑‍🤝‍🧑 2-1. 개발 인원
- 1인

### ⚙️ 2-2. 개발 환경
- Java 11
- JDK 1.8.0
- **IDE** : IntelliJ IDEA Community Edition 2021.1.2
- **Editor** : Visual Studio Code
- **Framework** : Spring Boot(2.6.3)
- **Database** : Mysql(8.0)
- **ORM** : JPA

## ⚖️ 3. 요구사항 정의
<img src = "https://user-images.githubusercontent.com/76469073/197168635-e2963cbd-65b3-4903-8393-72edcbf29afc.png" width="80%" height="80%">

## :blue_book: 4. DB 설계
<img src = "https://user-images.githubusercontent.com/76469073/197688829-791fd750-8459-40f3-a066-f8d84cf17c3f.png" width="80%" height="80%">

## :green_book: 5. API 설계
<img src = "https://user-images.githubusercontent.com/76469073/197172432-dd9f8cc6-0cf8-4cc8-adc9-6d96186a4fee.png" width="80%" height="80%">

## 📌 6. 주요 기능
### **:large_blue_circle: 6-1. 로그인 전 사용가능한 기능**
### [6-1-1. 페이지 및 검색 기능]
- 메인 페이지에 게시물들을 보여주고, 페이지를 통해 다른 페이지로 이동할 수 있다.
- 검색창에 키워드 검색 시 게시물의 제목을 기준으로 키워드에 맞는 게시물들을 출력해준다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197505805-32624177-dff5-4e8c-8bff-a8ad3b126ff6.png" width="80%" height="80%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197994068-b2c928f9-e33d-4a82-ac32-96d34181cd44.png" width="80%" height="80%">

</details>

<details>
 
<summary>[Code]</summary>

- MainController.java: Paging 알고리즘을 이용해 페이지를 구현한다.
  
```java
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(Criteria cri, ItemSearchDto itemSearchDto, Model model){

        //메인페이지 총 게시물개수
        List<MainItemDto> itemtotal=itemService.getMainItemListPage(itemSearchDto);

        //메인페이지 데이터를 가지고 올 시작 인덱스 및 한 번에 가지고 올 최대 개수
        List<MainItemDto> itemsShow=itemService.getMainItemListShowPage(itemSearchDto,cri);

        Paging paging=new Paging();
        paging.setCri(cri);
        paging.setTotalCount(itemtotal.size());

        model.addAttribute("paging", paging);
        model.addAttribute("itemtotal",itemtotal.size());
        model.addAttribute("items",itemsShow);

        double lastPage=Math.ceil((double) itemtotal.size()/cri.getPerPageNum()); // 전체 페이지의 마지막 페이지
        model.addAttribute("lastPage",lastPage);

        return "main";
    }
```
  
- ItemService.java
  
```java
    //메인페이지 총 게시물 개수
    @Transactional(readOnly = true)
    public List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto){
        return itemRepository.getMainItemListPage(itemSearchDto);
    }

    //메인페이지 데이터를 가지고 올 시작 인덱스 및 한 번에 가지고 올 최대 개수
    @Transactional(readOnly = true)
    public List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto,Criteria criteria){
        return itemRepository.getMainItemListShowPage(itemSearchDto,criteria);
    }
```
  
- ItemRepositoryImpl.java: Querydsl을 사용해 쿼리를 동적으로 생성한다.
  
```java
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null: QItem.item.title.like("%"+searchQuery+"%");
    }

    @Override
    public List<MainItemDto> getMainItemListPage(ItemSearchDto itemSearchDto) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;

                return queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.title,
                                item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetch();
    }

    @Override
    public List<MainItemDto> getMainItemListShowPage(ItemSearchDto itemSearchDto, Criteria criteria) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;
        
        return queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.title,
                                item.itemDetail,
                                itemImg.imgUrl)
                )
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(criteria.getPageStart()) //데이터를 가지고 올 시작 인데스
                .limit(criteria.getPerPageNum()) //한 번에 가지고 올 최대 개수
                .fetch();
    }
```
  
</details>

### [6-1-2. 게시물 자세히보기 기능]
- 메인페이지의 게시물에 대한 정보를 자세히 확인할 수 있다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197374738-309ee6fe-8222-4a65-9fe3-8a9db8b60664.png" width="60%" height="60%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197994556-5f5ec47b-a5ea-4372-93a2-b8cda60e8025.PNG" width="60%" height="60%">

</details>
  
<details>
<summary>[Code]</summary>

- MainController.java

```java
    //메인화면 사진 클릭시 처리될 api
    @GetMapping("/api/item")
    public ResponseEntity<?> MainItemInfo(MainItemDetailDto mainItemDetailDto){
        return new ResponseEntity<>(mainItemDetailDto, HttpStatus.OK);
    }
```

- main.js: Ajax를 이용해 게시물에 대한 정보를 불러온다.

```javascript
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
```
  
</details>

### [6-1-3. 구글 로그인 기능]
- 구글 로그인을 통해 로그인을 할 수 있다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197388397-0b824ad2-d630-400d-b246-624e75054b77.png" width="60%" height="60%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197994894-b9278054-fccc-4140-9e80-d8d1bd00b88e.PNG" width="60%" height="60%">

</details>

<details>
<summary>[Code]</summary>

- OAuthAttributes.java: 구글 로그인 후 가져온 사용자의 이름, 이메일, 프로필 사진 주소를 가져온다.

```java
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }


    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .createDate(LocalDateTime.now())
                .build();
    }
}

```

- CustomOAuth2UserService.java: OAuthAttributes를 기반으로 가입 진행

```java
@Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
```

- SecurityConfig.java: oauth2로 로그인할 수 있도록 설정

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile","/api/item").permitAll()
                .antMatchers("/user/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

    }   
```
</details>

------------------------------------------------------

### **:large_blue_circle: 6-2. 로그인 후 사용가능한 기능**
### [6-2-1. 사진 업로드 기능]
- 제목, 설명, 사진을 입력해 게시물을 업로드할 수 있다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197521578-dcfea743-1b1b-46de-8b2a-374c99a73dda.png" width="60%" height="60%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197995096-c01e07a1-bd57-419d-a81d-26b31384d8eb.PNG" width="60%" height="60%">

</details>

<details>
<summary>[Code]</summary>

- FileService.java: UUID 값과 원래 파일의 이름의 확장자를 합쳐 저장 파일 이름을 생성한다.
```java
    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData) throws Exception{

        UUID uuid=UUID.randomUUID();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName=uuid.toString()+extension;
        String fileUploadFullUrl=uploadPath+"/"+savedFileName;
        FileOutputStream fos=new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;

    }
```

- ItemImgService.java: 이미지가 로컬에 저장되고, 입력받은 이미지에 대한 정보가 DB에 저장된다.

```java
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName=itemImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName= fileService.uploadFile(itemImgLocaiton,oriImgName,itemImgFile.getBytes());
            imgUrl="/images/item/"+imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);

    }
```

- ItemService.java: 게시물을 등록하고, itemImgService 클래스를 이용해 이미지를 등록한다.

```java
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        SessionUser user=(SessionUser) httpSession.getAttribute("user");

        //상품 등록
        itemFormDto.setRegister(user.getEmail());
        Item item= itemFormDto.createItem();
        itemRepository.save(item);
        
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg=new ItemImg();
            itemImg.setItem(item);
            if(i==0) {
                itemImg.setRepImgYn("Y");
            } else{
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }

        return item.getId();
    }
```

- ItemController.java: 게시물 등록 전 값을 담을 itemFormDto를 전달해주고, 게시물에 대한 검증 후 게시물 등록된다.

```java
    @GetMapping(value = "/user/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/user/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult
    , Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번쨰 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        
        Long itemId; //아이템 아이디 전달하기 위해

        try {
            itemId=itemService.saveItem(itemFormDto,itemImgFileList); // 물품 등록
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

    }
```

</details>

### [6-2-2. 사진관리 기능]
- 구글 로그인을 통해 얻은 개인정보를 보여주고, 사용자가 업로드한 게시물들을 보여준다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197689938-1e61fa53-516d-4723-a8ba-0e3f69e77c3a.png" width="60%" height="60%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197995354-8aaab9ad-fc9a-4cbe-9a25-639b7b3ee7cb.PNG" width="60%" height="60%">

</details>

<details>
<summary>[Code]</summary>

- MyPageController.java: 사용자 정보와 사용자가 올린 총 게시물을 보여준다.
```java
    @GetMapping(value = "/user/items") //마이페이지
    public String itemMng(Criteria cri, Model model){

        SessionUser user=(SessionUser) httpSession.getAttribute("user");

        try {
            //마이페이지 총 게시물개수
            List<MyPageItemDto> itemtotal = myPageService.getItemMngListPage(user.getEmail());
            //마이페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
            List<MyPageItemDto> itemsShow = myPageService.getItemMngListShowPage(cri,user.getEmail());

            Paging paging=new Paging();
            paging.setCri(cri);
            paging.setTotalCount(itemtotal.size());

            model.addAttribute("paging", paging); //페이징 정보
            model.addAttribute("itemtotal",itemtotal.size()); //내가 업로드한 게시물 개수
            model.addAttribute("items",itemsShow);
            model.addAttribute("userName",user.getName()); //유저이름
            model.addAttribute("userEmail",user.getEmail()); //유저이메일
            model.addAttribute("userPicture",user.getPicture());//유저사진
            
            double lastPage=Math.ceil((double) itemtotal.size()/cri.getPerPageNum()); //; 전체 페이지의 마지막 페이지
            model.addAttribute("lastPage",lastPage);

        }catch (IllegalStateException e){
            model.addAttribute("userName",user.getName()); //유저이름
            model.addAttribute("userEmail",user.getEmail()); //유저이메일
            model.addAttribute("userPicture",user.getPicture());//유저사진
            model.addAttribute("itemtotal",0); //게시물 개수
            model.addAttribute("errorMessage",e.getMessage());
            return "item/itemMng";
        }

        return "item/itemMng";

    }
```

- MyPageService.java

```java
    @Transactional(readOnly = true)
    public List<MyPageItemDto> getItemMngListPage(String email) {
        
        User user=userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원의 마이페이지 엔티티 조회
        MyPage myPage=myPageRepository.findByUserId(user.getId());
        if(myPage==null){
            throw new IllegalStateException("업로드한 사진이 없습니다.");
        }

        //마이페이지에 담겨있는 물품 정보 조회
        return itemRepository.getItemMngListPage(myPage.getId());
    }

    @Transactional(readOnly = true)
    public List<MyPageItemDto> getItemMngListShowPage(Criteria criteria,String email) {

        User user=userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원의 마이페이지 엔티티 조회
        MyPage myPage=myPageRepository.findByUserId(user.getId());

        //마이페이지에 담겨있는 물품 정보 조회
        return itemRepository.getItemMngListShowPage(criteria,myPage.getId());
    }
```

- ItemRepositoryImpl.java

```java
    //사진관리페이지 총 게시물 수
    @Override
    public List<MyPageItemDto> getItemMngListPage(Long mypageId) {

        QMyPageItem myPageItem=QMyPageItem.myPageItem;
        QItemImg itemImg=QItemImg.itemImg;

        return queryFactory
                .select(
                        new QMyPageItemDto(
                                myPageItem.id,
                                myPageItem.item.title,
                                myPageItem.item.itemDetail,
                                itemImg.imgUrl,
                                myPageItem.item.register
                                )
                )
                .from(myPageItem,itemImg)
                .join(myPageItem.item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(myPageItem.myPage.id.eq(mypageId))
                .where(itemImg.item.id.eq(myPageItem.item.id))
                .fetch();

    }

    //사진관리페이지에 데이터를 가지고 올 시작 인데스 및 한 번에 가지고 올 최대 개수
    @Override
    public List<MyPageItemDto> getItemMngListShowPage(Criteria criteria,Long mypageId) {

        QMyPageItem myPageItem=QMyPageItem.myPageItem;
        QItemImg itemImg=QItemImg.itemImg;

        return queryFactory
                .select(
                        new QMyPageItemDto(
                                myPageItem.item.id,
                                myPageItem.item.title,
                                myPageItem.item.itemDetail,
                                itemImg.imgUrl,
                                myPageItem.item.register)
                )
                .from(myPageItem,itemImg)
                .join(myPageItem.item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(myPageItem.myPage.id.eq(mypageId))
                .where(itemImg.item.id.eq(myPageItem.item.id))
                .orderBy(myPageItem.regTime.desc())
                .offset(criteria.getPageStart())
                .limit(criteria.getPerPageNum())
                .fetch();

    }
```
  
</details>

### [6-2-3. 게시물 수정 및 삭제 기능]
- 업로드한 게시물을 수정하거나 삭제할 수 있다.
<details>
<summary>[View]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197521809-56d57075-78ea-4623-b2ce-76ff953495a2.png" width="80%" height="80%">

<img src = "https://user-images.githubusercontent.com/76469073/197522545-4b193507-36cc-439b-8b5f-b7825126121b.png" width="80%" height="80%">

</details>

<details>
<summary>[동작 순서]</summary>

<img src = "https://user-images.githubusercontent.com/76469073/197995711-4752c81a-c89a-4cba-8c77-f7532f0b1d0c.PNG" width="80%" height="80%">

</details>

<details>
<summary>[Code]</summary>

- ItemController.java: 업로드한 게시물의 정보를 가져온 뒤 수정한다.

```java
    //물품 수정시 물품 조회
    @GetMapping(value = "/user/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId,Model model){

        try{
            ItemFormDto itemFormDto=itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 물품 입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }
    
    //물품 수정 저장
    @PostMapping(value = "/user/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","물품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/user/items";

    }
```

- ItemService.java

```java
    //물품을 불러오는 메소드
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList=itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList=new ArrayList<>();

        for(ItemImg itemImg:itemImgList){ //조회한 itemImg 엔티티를 ItemImgDto 객체로 만들어서 추가
            ItemImgDto itemImgDto=ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item=itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto=ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }
  
    //물품 수정 메소드
    public Long updateItem(ItemFormDto itemFormDto,
                       List<MultipartFile> itemImgFileList) throws Exception{

        //물품 수정
        Item item=itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds=itemFormDto.getItemImgIds(); //상품 이미지 아이디 조회

        //이미지 수정
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }

        return item.getId();
    }
```

- ItemImgService.java

```java
    //물품 이미지 수정
    public void updateItemImg(Long itemImgId,MultipartFile itemImgFile) throws Exception{

        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg=itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocaiton+"/"+savedItemImg.getImgName());;
            }

            String oriImgName=itemImgFile.getOriginalFilename();
            String imgName= fileService.uploadFile(itemImgLocaiton,oriImgName,itemImgFile.getBytes());;
            String imgUrl="/images/item/"+imgName;
            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl); //수정된 물품 이미지 업데이트
        }

    }
```
----------------------------------------------------

- MyPageController.java: 업로드한 게시물을 삭제한다.

```java
   //마이페이지 사진(게시물) 삭제
   @DeleteMapping(value = "/user/item/delete/{itemId}")
    public ResponseEntity<?> ItemDelete(@PathVariable Long itemId) throws Exception {

       //로컬에 저장되어있는 사진, ItemImg 엔티티의 값 삭제
       itemImgService.deleteItemImg(itemId);

       //MyPageItem 엔티티의 값 삭제
       myPageService.deleteMyPageItem(itemId);

       //Item 엔티티의 값 삭제
       itemService.deleteItem(itemId);

       return new ResponseEntity<>(itemId,HttpStatus.OK);
  
    }
```

- ItemImgService.java: 로컬의 사진을 삭제하고, ItemImg 테이블에 있는 데이터를 삭제한다.

```java
    //사진 삭제 및 ItemImg 데이터 삭제
    public void deleteItemImg(Long itemId) throws Exception {
        List<ItemImg> itemImgs=itemImgRepository.findByItemId(itemId);

        List<Long> ItemImgIds=new ArrayList<>();

        //사진 삭제
        for(ItemImg itemImg:itemImgs){
            if(itemImg.getImgUrl()!=null){
                fileService.deleteFile(itemImgLocaiton+"/"+itemImg.getImgName());
            }
        }

        //ItemImg 데이터 삭제
        for(ItemImg itemImg:itemImgs){
            ItemImgIds.add(itemImg.getId());
        }

        itemImgRepository.deleteAllByIdQuery(ItemImgIds);
    }
```

- MyPageService.java: MyPageItem 테이블에 있는 데이터를 삭제한다.
  
```java
    //MyPageItem 삭제
    public void deleteMyPageItem(Long itemId) {
        MyPageItem myPageItem=myPageItemRepository.findByItemId(itemId);
        myPageItemRepository.delete(myPageItem);
    }
```

- ItemService.java: Item 테이블에 있는 데이터를 삭제한다.
  
```java
    //물품 삭제
    public void deleteItem(Long itemId){
        Item item=itemRepository.findById(itemId).get();
        itemRepository.delete(item);
    }
```
  
</details>
  
## :books: 7. 참고자료
- [Spring Boot + Thymeleaf] 스프링 부트 쇼핑몰 프로젝트 with JPA 책
- [구글 로그인] http://yoonbumtae.com/?p=2652
- [HTML + CSS + JavaScript] https://github.com/jeonghyun051/Spring-Instagram
