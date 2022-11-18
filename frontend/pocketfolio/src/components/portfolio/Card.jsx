import {React, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useDispatch} from 'react-redux';
import {
  Wrapper,
  Item,
  PortDiv,
  ImgDiv,
  TitleDiv,
  Title,
  Thumbnail,
  IconDiv,
  DeleteIcon,
} from './Card.style';
import DeleteModal from './DeleteModal';
import { delRoom } from '../../store/roomSlice';

const Card = props => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [isOpen, setIsOpen] = useState(false);
  const {isDelete, pocketData, reLander, setReLander} = props;
  const openModal = () => {
    setIsOpen(true);
  };
  const moveMyRoom = () => {
    navigate(`/room/${pocketData.roomSeq}`);
  };


  const deletePocket = (seq) => {
    console.log(seq)
    dispatch(delRoom(seq))
      .then(res => {
        if(res.payload) {
          setReLander(!reLander)
        }
      })
  }
  return (
    <Wrapper>
      <Item>
        <IconDiv className={isDelete ? 'delete' : ''} onClick={openModal}>
          <DeleteIcon></DeleteIcon>
        </IconDiv>

        <PortDiv onClick={moveMyRoom}>
          <ImgDiv>
            {pocketData.thumbnail === undefined ? (
              <Thumbnail
                src={process.env.PUBLIC_URL + '/assets/images/room_01.png'}
              />
            ) : (
              <Thumbnail src={pocketData.thumbnail} />
            )}
          </ImgDiv>
          <TitleDiv>
            <Title>{pocketData.name}</Title>
          </TitleDiv>
        </PortDiv>
      </Item>
      {isOpen && (
        <DeleteModal
          onClose={() => {
            setIsOpen(false);
          }}
          text={'포켓을'}
          seq={pocketData.roomSeq}
          deleteFunc={deletePocket}
        />
      )}
    </Wrapper>
  );
};

export default Card;
