import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import postApi from "../api/postApi";

function PostListPage() {
  const { boardId } = useParams();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    (async () => {
      try {
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
      <h1>Post List</h1>
      <ul>
        {posts.map((post) => (
          <li key={post.id}>
            <Link to={`/posts/${post.id}`}>{post.title}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PostListPage;
