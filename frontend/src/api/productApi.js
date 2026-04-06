import api from './index'; // 8080/api

const productApi = {
    // 상품 목록 조회
    getProducts:(page =0, size = 10) => {
        return api.get('/products', {
            params: {page, size}
            
        });
    },


// 상품 상세 조회
getProduct: (id) => {
    return api.get(`/products/${id}`);
    },

// 상품 등록
createProduct : (productData) => {
    return api.post(`/products`, productData);
},
// 상품 수정
updateProduct: (id, productData) => {
    return api.put(`/products/${id}`, productData);
},
// 상품 삭제
deleteProduct : (id) => {
    return api.delete(`/products/${id}`);
},

// 상품 검색(Keyword)
searchProducts:(keyword, page =0, size =10)=>{
    return api.get('/products/search',{
        params: {keyword, page, size}
    });
},
// 최신 상품 10개
getLatestProducts: () => {
    return api.get('/products/latest')
},

// 구매 가능 상품
getAvailableProducts: (page =0 , size =10) => {
    return api.get('/products/available', {params: {page,size}});

},

// 카테고리별 상품
getProductByCategory: (category, page=0, size=10) => {
    return api.get(`/products/category/${category}`,{
        params:{page,size}
    });
},
};
export default productApi;