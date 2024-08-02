$.ajax({
    type:"get",
    url:"/getMovieCategory",
    success:function (data){
        console.log(data);
        movie_category_create(data);

    },
    error:function(err,status){
        console.log(err,status);
        alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
    }
});
function getMovieCategory_List(e){
    e.preventDefault();
    if(e.target.tagName !="A") return;
    let dataset=$(e.target).data("set");



}