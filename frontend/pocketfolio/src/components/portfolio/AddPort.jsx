import React, {useState, useCallback} from 'react';
import {
  Wrapper,
  Background,
  Header,
  TitleDiv,
  Title,
  Img,
  HashDiv,
  InputDiv,
  HashInput,
  HashOutter,
  HashList,
} from './AddPort.style';
import Nav from '../common/nav';
import Editor from './Editor.test';

const AddPort = () => {
  // 포트폴리오 제목, 내용 변수
  const [portContent, setPortContent] = useState({
    title: '',
    content: '',
  });

  // 입력창에 쓰인 해시태그
  const [hashtag, setHashtag] = useState('');
  // 등록한 해시태그
  const [hashArr, setHashArr] = useState([]);

  // 포트폴리오 제목 저장
  const getValue = e => {
    const {name, value} = e.target;
    setPortContent({
      ...portContent,
      [name]: value,
    });
    console.log(portContent);
  };

  // 해시태그 입력
  const onChangeHashtag = e => {
    setHashtag(e.target.value);
  };

  // 해시태그 입력창에서 엔터 눌렀을 때,
  const onKeyUp = useCallback(e => {
    // 해시태그 배열에 추가 후 입력 창 초기화
    if (e.keyCode === 13 && e.target.value.trim() !== '') {
      setHashArr(hashArr => [...hashArr, hashtag]);
      setHashtag('');
    }
  });

  console.log(hashtag, hashArr);

  return (
    <Background>
      {/* <Nav></Nav> */}
      <Wrapper className="wrapper">
        <Header>포트폴리오 관리하기</Header>

        <TitleDiv>
          <Img
            className="pencil"
            src={process.env.PUBLIC_URL + '/assets/images/pencil.png'}
          ></Img>
          <Title
            className="title"
            type="text"
            autoComplete="off"
            placeholder="포트폴리오 제목"
            onChange={getValue}
            name="title"
          ></Title>
        </TitleDiv>

        <HashDiv className="HashWrap">
          <InputDiv>
            <Img
              className="hashtag"
              src={process.env.PUBLIC_URL + '/assets/images/hash2.png'}
            ></Img>
            <HashInput
              className="HashInput"
              type="text"
              value={hashtag}
              onChange={onChangeHashtag}
              onKeyUp={onKeyUp}
              placeholder="# 해시태그 입력"
            />
          </InputDiv>
          <HashList>
            {hashArr.map(item => (
              <HashOutter># {item}</HashOutter>
            ))}
          </HashList>
        </HashDiv>
        <Editor portContent={portContent} setPortContent={setPortContent} />
      </Wrapper>
    </Background>
  );
};

export default AddPort;
