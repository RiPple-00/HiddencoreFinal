// import { useState, useEffect } from 'react';
// import { Link } from 'react-router-dom'; // Link 페이지 이동 <a href>와 유사하다
// //import productApi from '../api/productApi'; // Axios기반 API호출

// function HomePage() {
//     // 상품 목록 데이터(배열) 초기값은 빈배열
//     const [latestProducts, setLatestProducts] = useState([]);
//     // 로딩 여부 초기값은 true
//     const [loading, setLoading] = useState(true);

//     useEffect(() => {
//         fetchLatestProducts();
//     }, []); // 의존성 배열 (빈배열 = 최초 1회만 실행)

//     const fetchLatestProducts = async () => {
//         try {
//             setLoading(true); // 로딩 시작
//             const response = await productApi.getLatestProducts(); // API 호출
//             setLatestProducts(response.data); // 데이터 저장
//         } catch (error) {
//             console.error('상품 조회 실패:', error);
//         } finally {
//             setLoading(false);
//         }
//     };

//     if (loading) {
//         return (
//             <div className="flex justify-center items-center h-64">
//                 <div className="text-gray-500">로딩중...</div>
//             </div>
//         );
//     }

//     return (
//         <div>
//             {/* 히어로 섹션 */}
//             <div className="bg-gradient-to-r from-blue-500 to-purple-600 text-white rounded-2xl p-12 mb-8">
//                 <h1 className="text-4xl font-bold mb-4">
//                     Spring Boot + React 쇼핑몰
//                 </h1>
//                 <p className="text-xl mb-6 opacity-90">
//                     최신 상품을 만나보세요!
//                 </p>
//                 <Link
//                     to="/products"
//                     className="inline-block bg-white text-blue-600 px-6 py-3 rounded-lg font-semibold hover:bg-gray-100 transition"
//                 >
//                     상품 둘러보기 →
//                 </Link>
//             </div>

//             {/* 최신 상품 섹션 */}
//             <h2 className="text-2xl font-bold mb-6">🆕 최신 상품</h2>

//             {latestProducts.length === 0 ? (
//                 <div className="text-center py-12 text-gray-500">
//                     등록된 상품이 없습니다.
//                 </div>
//             ) : (
//                 <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
//                     {latestProducts.map((product) => (
//                         <div
//                             key={product.id}
//                             className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition"
//                         >
//                             {/* 상품 이미지 */}
//                             <div className="h-48 bg-gray-200 flex items-center justify-center">
//                                 {product.imageUrl ? (
//                                     <img
//                                         src={product.imageUrl}
//                                         alt={product.name}
//                                         className="w-full h-full object-cover"
//                                     />
//                                 ) : (
//                                     <span className="text-4xl">📦</span>
//                                 )}
//                             </div>

//                             {/* 상품 정보 */}
//                             <div className="p-4">
//                                 <h3 className="font-semibold text-lg mb-2 truncate">
//                                     {product.name}
//                                 </h3>
//                                 <p className="text-blue-600 font-bold text-xl">
//                                     {product.price?.toLocaleString()}원
//                                     {/* toLocaleString()} -> 10000 => 10,000
//                                         roduct.price? => null일때 undefinded */}
//                                 </p>
//                                 <p className="text-sm text-gray-500 mt-2">
//                                     재고: {product.stockQuantity}개
//                                 </p>
//                             </div>
//                         </div>
//                     ))}
//                 </div>
//             )}
//         </div>
//     );
// }

// export default HomePage;