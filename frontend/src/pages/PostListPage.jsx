import { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import boardApi from '../api/postApi';
import button from '../components/Button';

function BoardListPage() {

    const { boardId } = useParams();
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    // 게시판 정보 가져오기
    useEffect(() => {
        (async () => {
            try {
                const res = await boardApi.getBoard(boardId);
                setBoard(res.data);
            } catch (e) {
                console.error(e);
            }
        })();
    }, [boardId]);

    // 게시글 목록 가져오기
    useEffect(() => {
        (async () => {
            try {
                setLoading(true);
                const res = await postApi.getPostsByBoard(boardId, page, 10);
                setPosts(res.data.content);
                setTotalPages(res.data.totalPages);
            } catch (e) {
                console.error(e);
            } finally {
                setLoading(false);
            }
        })();
    }, [boardId, page]);

    return (
        <div>


        </div>
    )

}

export default BoardListPage;