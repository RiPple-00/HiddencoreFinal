import { useState } from "react";

function PostListPage() {
    const { boardId } = useParams();
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);


}; export default PostListPage;