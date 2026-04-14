<<<<<<< HEAD
import { useState } from "react";

function PostListPage() {
    const { boardId } = useParams();
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);


}; export default PostListPage;
=======
import { Navigate, useParams } from 'react-router-dom';

/**
 * 예전 단순 목록 페이지는 시설 게시판(BoardListPage)으로 통합됨.
 * 혹시 남은 링크는 시설 게시판으로 보냄.
 */
function PostListPage() {
  const { facilityId, boardId } = useParams();
  const id = facilityId ?? boardId ?? '1';
  return <Navigate to={`/facilities/${id}/board`} replace />;
}

export default PostListPage;
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
