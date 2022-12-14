import styled from 'styled-components';

export const Container = styled.div`
  width: 100vw;
  display: flex;
  flex-direction: row;
  justify-content: left;
  align-items: center;

  position: relative;

  overflow: hidden;

  @media screen and (max-width: 1200px) {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    &.active {
      width: 100vw;
      height: auto;
      transition: all 0.25s;
    }
  }
`;

export const CanvasWrapper = styled.div`
  width: 100vw;
  height: 100vh;
  position: relative;

  &.active {
    width: calc(100vw - 40rem);
    transition: all 0.25s;
  }

  @media screen and (max-width: 1200px) {
    width: 100vw;
    height: 100vh;

    &.active {
      width: 100vw;
      height: 60vh;
      transition: all 0.25s;
    }
  }
`;

export const EditBox = styled.div`
  position: absolute;
  bottom: 1.5rem;
  right: 1.5rem;

  display: flex;
  align-items: center;
`;

export const Btn = styled.button`
  background-color: #e75452;
  color: white;
  border-radius: 1.5rem;
  padding: 0.5rem 0.7rem;
  font-size: 1.2rem;
  border: 1.5px solid #e75452;

  & + & {
    margin-left: 0.5rem;
  }

  &:hover {
    border: 1.5px solid #c74745;
    background-color: #c74745;
    color: #dfdfdf;
  }

  &.cancel {
    color: #e75452;
    border: 1.5px solid #e75452;
    background-color: white;

    &:hover {
      color: #c74745;
      border: 1.5px solid #c74745;
      background-color: #dfdfdf;
    }
  }
`;
