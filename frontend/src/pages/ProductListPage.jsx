import { useState, useEffect} from 'react'
import productApi from '../api/productApi'

function ProductListPage(){

    const [products, setProducts] = useState([]); // 상품 목록 배열
    const [loading, setLoading] = useState(true); //로딩 여부
    const [page, setPage] = useState(0); // 현재 페이지 번호
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const [searchKeyword, setSearchKeyword] = useState(''); // 검색 키워드 수

    useEffect(() => {
        fetchProducts();
    }, [page]); // page 바뀔때마다 fetchProducts 호출


    // 상품 목록 API 호출ㅌㅋ
    const fetchProducts = async () => {
        try{
            setLoading(true);
            const response = await productApi.getProducts(page, 12); // 페이지 번호 12개씩
            setProducts(response.data.content); // 상품 배열
            setTotalPages(response.data.totalPages); // 전체 페이지 수
        }catch (error) {
            console.error('상품 조회 실패:', error);
        }finally{
            setLoading(false);
        }
    };
    // 검색 처리
    const handleSearch = async (e) => {
        e.preventDefault(); // form 기본 동작(페이지 새로고침) 차단
        if(!searchKeyword.trim()){ // 검색어가 공백이면
            fetchProducts(); // 전체 목록으로 들어가기
            return
        }
        try {
            setLoading(true);
            const response = await productApi.searchProducts(searchKeyword, 0, 12);
            setProducts(response.data.content);
            setTotalPages(response.data.totalPages);
            setPage(0); // 검색 시 항상 1페이지로 리셋 
        } catch (error) {
            console.error('검색 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    return(
        <div>
            <h1 className="text-3xl font-bold mb-8 text-orange-700">📦 상품 목록</h1>
        {/* 검색 폼 */}
            <form onSubmit={handleSearch} className="mb-8">
                <div className="flex gap-2">
                    <input
                        type="text"
                        value={searchKeyword}
                        onChange={(e) => setSearchKeyword(e.target.value)}
                        placeholder="상품명으로 검색..."
                        className="flex-1 px-4 py-3 border-2 border-orange-200 rounded-full focus:ring-2 focus:ring-emerald-400 focus:border-emerald-400 outline-none bg-orange-50"
                    />
                    <button
                        type="submit"
                        className="px-6 py-3 bg-gradient-to-r from-emerald-400 to-teal-500 text-white rounded-full hover:from-emerald-500 hover:to-teal-600 transition shadow-md font-medium"
                    >
                        검색
                    </button>
                </div>
            </form>

            

{/* 로딩 */}
            {loading ? (
                <div className="flex justify-center items-center h-64">
                    <div className="text-orange-400">로딩 중...</div>
                </div>
            ) : products.length === 0 ? (
                <div className="text-center py-12 text-gray-500">
                    상품이 없습니다.
                </div>
            ) : (
                <>
                    {/* 상품 그리드 */}
                    <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6 mb-8">
                        {products.map((product) => (
                            <div
                                key={product.id}
                                className="bg-white rounded-2xl shadow-sm overflow-hidden hover:shadow-md transition cursor-pointer border border-orange-100"
                            >
                                <div className="h-48 bg-orange-50 flex items-center justify-center">
                                    {product.imageUrl ? (
                                        <img
                                            src={product.imageUrl}
                                            alt={product.name}
                                            className="w-full h-full object-cover"
                                        />
                                    ) : (
                                        <span className="text-4xl">📦</span>
                                    )}
                                </div>
                                <div className="p-4">
                                    <div className="flex items-center gap-2 mb-2">
                                        <span className={`px-2 py-1 text-xs rounded-full font-medium ${
                                            product.status === 'ACTIVE' 
                                                ? 'bg-emerald-100 text-emerald-800' 
                                                : product.status === 'OUT_OF_STOCK'
                                                ? 'bg-orange-100 text-orange-800'
                                                : 'bg-gray-100 text-gray-800'
                                        }`}>
                                            {product.status === 'ACTIVE' ? '판매중' : 
                                             product.status === 'OUT_OF_STOCK' ? '품절' : '판매중지'}
                                        </span>
                                        {product.category && (
                                            <span className="text-xs text-gray-600">
                                                {product.category}
                                            </span>
                                        )}
                                    </div>
                                    <h3 className="font-semibold text-lg mb-2 truncate text-gray-800">
                                        {product.name}
                                    </h3>
                                    <p className="text-emerald-600 font-bold text-xl">
                                        {product.price?.toLocaleString()}원
                                    </p>
                                    <p className="text-sm text-gray-500 mt-2">
                                        재고: {product.stockQuantity}개
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                    {/* 페이지네이션 */}
                    {totalPages > 1 && (
                        <div className="flex justify-center gap-2">
                            <button
                                onClick={() => setPage(page - 1)}
                                disabled={page === 0}
                                className="px-4 py-2 border-2 border-orange-200 rounded-full disabled:opacity-50 disabled:cursor-not-allowed hover:bg-orange-50 text-gray-700 font-medium transition"
                            >
                                이전
                            </button>
                            <span className="px-4 py-2 font-medium text-gray-700">
                                {page + 1} / {totalPages}
                            </span>
                            <button
                                onClick={() => setPage(page + 1)}
                                disabled={page >= totalPages - 1}
                                className="px-4 py-2 border-2 border-orange-200 rounded-full disabled:opacity-50 disabled:cursor-not-allowed hover:bg-orange-50 text-gray-700 font-medium transition"
                            >
                                다음
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>

    )
}

export default ProductListPage