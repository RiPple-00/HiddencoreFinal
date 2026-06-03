# # =============================================================
# #  따숨 요양원 RAG 챗봇 (섹션 헤더 수정본)
# #  수정 내용:
# #   - 각 청크에 섹션 헤더를 자동으로 붙여서
# #     "시설에 대해"처럼 넓은 질문도 정확히 검색되도록 개선
# # =============================================================
# # 설치:
# #   pip install langchain langchain-openai langchain-chroma
# #              langchain-text-splitters pypdf python-dotenv
# # ⚠️ 기존 chroma_db 폴더 삭제 후 실행!
# # =============================================================

# import os
# import re
# from dotenv import load_dotenv
# from langchain_community.document_loaders import PyPDFLoader
# from langchain_text_splitters import RecursiveCharacterTextSplitter
# from langchain_openai import OpenAIEmbeddings, ChatOpenAI
# from langchain_chroma import Chroma
# from langchain_core.documents import Document
# from langchain_core.prompts import ChatPromptTemplate
# from langchain_core.output_parsers import StrOutputParser

# # =============================================================
# # Step 1. 환경변수
# # =============================================================

# load_dotenv()
# OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
# if OPENAI_API_KEY:
#     print("✅ API Key 로드 완료:", OPENAI_API_KEY[:8] + "...")
# else:
#     raise ValueError("❌ .env 파일에 OPENAI_API_KEY를 추가하세요.")

# BASE_DIR   = os.path.dirname(os.path.abspath(__file__))
# PDF_PATH   = os.path.join(BASE_DIR, "data", "따숨요양원_안내서_최종완성.pdf")
# CHROMA_DIR = os.path.join(BASE_DIR, "chroma_db")

# # =============================================================
# # Step 2. PDF 로드 + 텍스트 정제
# # =============================================================

# print(f"\n📄 PDF 로딩 중: {PDF_PATH}")
# pages = PyPDFLoader(PDF_PATH).load()
# print(f"✅ {len(pages)}페이지 로드 완료")

# def clean_text(text: str) -> str:
#     text = text.replace("\x00", " ")
#     text = re.sub(r"[\x01-\x08\x0b-\x1f]", " ", text)
#     text = re.sub(r" {2,}", " ", text)
#     return text.strip()

# for doc in pages:
#     doc.page_content = clean_text(doc.page_content)

# # =============================================================
# # Step 3. ✅ 핵심 수정: 섹션 헤더를 각 청크 앞에 붙이기
# #
# #  PDF 구조에 맞는 섹션 이름을 미리 정의해두고,
# #  각 페이지가 어느 섹션에 속하는지 자동으로 감지해서
# #  청크 앞에 "[섹션명]" 태그를 붙임
# #
# #  예) "[시설 소개] 따숨 요양원은 어르신 한 분 한 분의..."
# #      → "시설에 대해 설명해줘" 질문과 잘 매칭됨
# # =============================================================

# # 따숨 PDF의 섹션 키워드 → 섹션 이름 매핑
# SECTION_KEYWORDS = {
#     "시설 소개":          "시설 소개",
#     "오시는 길":          "오시는 길 (교통편, 주소, 위치)",
#     "주요 연락처":        "주요 연락처",
#     "주차 안내":          "주차 안내",
#     "주차 요금":          "주차 요금",
#     "차량 높이 제한":     "차량 높이 제한",
#     "차량 등록 방법":     "차량 등록 방법",
#     "전기차 충전":        "전기차 충전 구역",
#     "서류 안내":          "서류 안내",
#     "무방문":             "무방문(비대면) 서류 발급",
#     "증명서 발급":        "증명서 발급",
#     "의무기록사본":       "의무기록사본 발급",
#     "원무 안내":          "원무 안내",
#     "퇴원환자 서비스":    "퇴원환자 서비스",
#     "퇴원절차":           "퇴원 절차",
#     "면회 신청":          "면회 신청 기간",
#     "면회 시간":          "면회 시간",
#     "입원절차":           "입원 절차",
#     "원무팀 전화번호":    "원무팀 전화번호",
#     "입원 준비물":        "입원 준비물",
#     "보호자 준비사항":    "보호자 준비사항",
#     "입원 가능 시간":     "입원 가능 시간",
#     "응급 입원":          "응급 입원 여부",
# }

# def detect_section(text: str) -> str:
#     """텍스트에서 섹션 이름을 감지"""
#     for keyword, section_name in SECTION_KEYWORDS.items():
#         if keyword in text:
#             return section_name
#     return ""

# def add_section_header(docs: list) -> list:
#     """
#     각 페이지의 섹션을 감지하고,
#     청크 앞에 '[섹션명]' 태그를 붙여서 반환
#     """
#     result = []
#     current_section = ""
#     for doc in docs:
#         detected = detect_section(doc.page_content)
#         if detected:
#             current_section = detected

#         if current_section:
#             # 청크 앞에 섹션명 태그 추가
#             doc.page_content = f"[{current_section}]\n{doc.page_content}"
#             doc.metadata["section"] = current_section

#         result.append(doc)
#     return result

# pages = add_section_header(pages)
# print("✅ 섹션 헤더 태깅 완료")

# # =============================================================
# # Step 4. 청킹
# # =============================================================

# splitter = RecursiveCharacterTextSplitter(
#     chunk_size=600,
#     chunk_overlap=100,
#     separators=["\n\n", "\n", ".", " "],
# )
# chunks = splitter.split_documents(pages)
# chunks = [c for c in chunks if len(c.page_content.strip()) > 30]
# print(f"✅ 청킹 완료: {len(pages)}페이지 → {len(chunks)}개 조각")

# # =============================================================
# # Step 5. 임베딩 + Chroma DB
# # =============================================================

# embeddings = OpenAIEmbeddings(model="text-embedding-3-small")

# if os.path.exists(CHROMA_DIR) and os.listdir(CHROMA_DIR):
#     print(f"\n📂 기존 Chroma DB 로드 중...")
#     db = Chroma(persist_directory=CHROMA_DIR, embedding_function=embeddings)
#     print("✅ DB 로드 완료!")
# else:
#     print(f"\n🔢 임베딩 생성 중... (최초 1회)")
#     db = Chroma.from_documents(
#         documents=chunks,
#         embedding=embeddings,
#         persist_directory=CHROMA_DIR,
#     )
#     print(f"✅ Chroma DB 저장 완료! ({len(chunks)}개 chunk)")

# # =============================================================
# # Step 6. LLM + 프롬프트
# # =============================================================

# llm = ChatOpenAI(model="gpt-4o-mini", temperature=0)

# PROMPT = ChatPromptTemplate.from_template("""
# 당신은 따숨 요양원의 친절한 안내 챗봇입니다.
# 아래 [참고 내용]을 바탕으로 질문에 답하세요.

# 규칙:
# 1. [참고 내용]에 관련 정보가 있으면 → 친절하고 명확하게 안내
# 2. [참고 내용]에 관련 정보가 없으면 → "안내서에서 확인되지 않는 내용입니다. 원무과(02-6901-7098)로 문의해 주세요." 라고만 답변
# 3. 절대 추측하거나 내용을 꾸며내지 마세요

# [참고 내용]
# {context}

# 질문: {question}

# 답변:""")

# chain = PROMPT | llm | StrOutputParser()
# print("✅ 챗봇 준비 완료!\n")

# # =============================================================
# # Step 7. 질문 함수
# # =============================================================

# def ask(question: str, verbose: bool = False) -> str:
#     print(f"{'='*50}")
#     print(f"❓ 질문: {question}")

#     docs    = db.similarity_search(question, k=4)
#     context = "\n\n".join([doc.page_content for doc in docs])

#     if verbose:
#         print("\n📄 검색된 섹션:")
#         for doc in docs:
#             section = doc.metadata.get("section", "미분류")
#             print(f"  - [{section}] {doc.page_content[:60]}...")
#         print()

#     answer = chain.invoke({"context": context, "question": question})
#     print(f"🏥 답변:\n{answer}\n")
#     return answer

# # =============================================================
# # Step 8. 대화형 실행
# # =============================================================

# def run_chatbot():
#     print("\n" + "="*50)
#     print("🏥 따숨 요양원 챗봇에 오신 것을 환영합니다!")
#     print("   종료: 'q' 또는 '종료'")
#     print("="*50)
#     while True:
#         user_input = input("\n💬 질문: ").strip()
#         if not user_input:
#             continue
#         if user_input.lower() in ["q", "quit", "종료", "exit"]:
#             print("👋 챗봇을 종료합니다.")
#             break
#         ask(user_input)

# # =============================================================
# # Step 9. 테스트
# # =============================================================

# if __name__ == "__main__":
#     ask("시설에 대해 설명해줄 수 있어요?")    # ← 이제 정상 답변 나와야 함
#     ask("면회 시간이 언제예요?")
#     ask("주차 요금은 얼마예요?")
#     ask("전기차 충전 구역은 어디 있나요?")
#     ask("의무기록 사본은 어떻게 발급받나요?")
#     ask("지하철을 이용해서 가려면 어떻게 가면되나요?")
#     ask("요양원에서 치매 예방 운동을 추천해 주세요.")  # PDF에 없는 내용

#     # run_chatbot()
    
# -------------------------------------------------------------------------

# ai/chatbot/chatbot.py
# FastAPI + OpenAI GPT + ChromaDB RAG (PDF 직접 지원)

import os
import re
from pathlib import Path
from typing import List

import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from dotenv import load_dotenv
from openai import OpenAI
import chromadb
from chromadb.utils import embedding_functions

# ── 환경 변수 ──────────────────────────────────────────────────
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY", "").strip()
if not OPENAI_API_KEY:
    raise RuntimeError("❌ .env 파일에 OPENAI_API_KEY 가 없습니다.")

# ── 경로 설정 ──────────────────────────────────────────────────
BASE_DIR   = Path(__file__).parent
DATA_DIR   = BASE_DIR / "data"
CHROMA_DIR = BASE_DIR / "chroma_db"
COLLECTION = "thasoom_rag"

# ── OpenAI + ChromaDB 초기화 ───────────────────────────────────
openai_client = OpenAI(api_key=OPENAI_API_KEY)

chroma_client = chromadb.PersistentClient(path=str(CHROMA_DIR))
embed_fn = embedding_functions.OpenAIEmbeddingFunction(
    api_key=OPENAI_API_KEY,
    model_name="text-embedding-3-small",
)
collection = chroma_client.get_or_create_collection(
    name=COLLECTION,
    embedding_function=embed_fn,
)

SYSTEM_PROMPT = """
당신은 따숨 요양원의 전담 AI 상담사입니다.
친절하고 따뜻한 말투로 어르신 보호자분들의 질문에 답변해 주세요.

[답변 원칙]
1. 제공된 참고 문서 내용을 최우선으로 활용하세요.
2. 문서에 없는 내용은 "담당자에게 직접 문의 바랍니다 (02-6901-7098)" 라고 안내하세요.
3. 전화번호·시간·절차 등 정확한 정보는 반드시 문서 기반으로만 답변하세요.
4. 답변은 간결하고 명확하게 작성하세요.
5. 단계가 있는 내용은 번호 목록으로 안내하세요.

[시설 기본 정보]
- 이름: 따숨 요양원
- 주소: 서울특별시 종로구 종로12길 15 (관철동 13-13)
- 대표번호: 02-6901-7098
- 오시는 길: 지하철 1호선 종각역 4번 출구 도보 5분
""".strip()


def extract_pdf_text(pdf_path: Path) -> str:
    try:
        from pypdf import PdfReader
        reader = PdfReader(str(pdf_path))
        pages_text = []
        for page in reader.pages:
            text = page.extract_text() or ""
            text = re.sub(r"[ \t]{2,}", " ", text)
            text = re.sub(r"\n{3,}", "\n\n", text)
            pages_text.append(text.strip())
        return "\n\n".join(t for t in pages_text if t)
    except ImportError:
        raise RuntimeError("pypdf 가 없습니다. pip install pypdf 실행하세요.")


def chunk_text(text: str, chunk_size: int = 600, overlap: int = 100) -> List[str]:
    section_pattern = r"(?=\n(?:요양원 안내|주차 안내|서류 안내|원무 안내|진료 및 입원|시설 소개|오시는 길|주요 연락|주차 요금|차량 높이|차량 등록|전기차|증명서 발급|의무기록|퇴원|면회|입원절차|원무팀|입원 준비|보호자 준비|응급 입원))"
    sections = re.split(section_pattern, text)
    chunks = []
    for section in sections:
        section = section.strip()
        if len(section) < 30:
            continue
        if len(section) <= chunk_size:
            chunks.append(section)
        else:
            start = 0
            while start < len(section):
                chunks.append(section[start:start + chunk_size])
                start += chunk_size - overlap
    return [c for c in chunks if len(c.strip()) > 30]


def load_documents():
    if collection.count() > 0:
        print(f"[ChromaDB] ✅ 기존 {collection.count()}개 청크 로드됨 — 색인 스킵")
        return

    all_chunks, all_ids, all_metas = [], [], []

    for pdf_path in sorted(DATA_DIR.glob("*.pdf")):
        print(f"[색인] PDF: {pdf_path.name}")
        try:
            text = extract_pdf_text(pdf_path)
            chunks = chunk_text(text)
            for i, chunk in enumerate(chunks):
                all_chunks.append(chunk)
                all_ids.append(f"{pdf_path.stem}_{i}")
                all_metas.append({"source": pdf_path.name, "chunk": i})
            print(f"       → {len(chunks)}개 청크")
        except Exception as e:
            print(f"[경고] {e}")

    for ext in ["*.txt", "*.md"]:
        for fpath in sorted(DATA_DIR.glob(ext)):
            print(f"[색인] TXT: {fpath.name}")
            text = fpath.read_text(encoding="utf-8")
            chunks = chunk_text(text)
            for i, chunk in enumerate(chunks):
                all_chunks.append(chunk)
                all_ids.append(f"{fpath.stem}_{i}")
                all_metas.append({"source": fpath.name, "chunk": i})

    if not all_chunks:
        print(f"[경고] {DATA_DIR} 에 문서가 없습니다.")
        return

    batch_size = 50
    for i in range(0, len(all_chunks), batch_size):
        collection.add(
            documents=all_chunks[i:i+batch_size],
            ids=all_ids[i:i+batch_size],
            metadatas=all_metas[i:i+batch_size],
        )
    print(f"[ChromaDB] ✅ {len(all_chunks)}개 청크 색인 완료!")


def retrieve_context(query: str, top_k: int = 5) -> str:
    if collection.count() == 0:
        return ""
    results = collection.query(query_texts=[query], n_results=top_k)
    docs = results.get("documents", [[]])[0]
    return "\n\n---\n\n".join(docs) if docs else ""


app = FastAPI(title="따숨 요양원 챗봇 API", version="2.0.0")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class Message(BaseModel):
    role: str
    content: str

class ChatRequest(BaseModel):
    messages: List[Message]

class ChatResponse(BaseModel):
    reply: str
    sources: List[str] = []


@app.get("/health")
def health():
    return {"status": "ok", "docs_indexed": collection.count()}


@app.post("/chat", response_model=ChatResponse)
async def chat(req: ChatRequest):
    if not req.messages:
        raise HTTPException(status_code=400, detail="messages 가 비어 있습니다.")

    last_user_msg = next(
        (m.content for m in reversed(req.messages) if m.role == "user"), ""
    )

    context = retrieve_context(last_user_msg)
    system_content = SYSTEM_PROMPT
    if context:
        system_content += f"\n\n[참고 문서]\n{context}"

    history = [{"role": m.role, "content": m.content} for m in req.messages[-10:]]

    response = openai_client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[{"role": "system", "content": system_content}, *history],
        temperature=0.3,
        max_tokens=700,
    )

    reply = response.choices[0].message.content.strip()

    sources: List[str] = []
    if context:
        results = collection.query(query_texts=[last_user_msg], n_results=3)
        metas = results.get("metadatas", [[]])[0]
        sources = list({m["source"] for m in metas if "source" in m})

    return ChatResponse(reply=reply, sources=sources)


@app.on_event("startup")
def startup():
    print("\n" + "="*50)
    print("🏥 따숨 요양원 챗봇 서버 시작")
    print("="*50)
    load_documents()
    print("✅ 서버 준비 완료 — http://localhost:8000\n")


if __name__ == "__main__":
    uvicorn.run("chatbot:app", host="0.0.0.0", port=8000, reload=True)