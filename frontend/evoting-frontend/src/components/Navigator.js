import * as React from 'react';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Box from '@mui/material/Box';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import HomeIcon from '@mui/icons-material/Home';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import PermContactCalendarRoundedIcon from '@mui/icons-material/PermContactCalendarRounded';
import HowToVoteRoundedIcon from '@mui/icons-material/HowToVoteRounded';
import BarChartRoundedIcon from '@mui/icons-material/BarChartRounded';
import InfoRoundedIcon from '@mui/icons-material/InfoRounded';
import Chip from '@mui/material/Chip';
import { useQuery } from '@tanstack/react-query';
import Axios from "axios";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

const item = {
  py: '2px',
  px: 3,
  color: 'rgba(255, 255, 255, 0.7)',
  '&:hover, &:focus': {
    bgcolor: 'rgba(255, 255, 255, 0.08)',
  },
};

const itemCategory = {
  boxShadow: '0 -1px 0 rgb(255,255,255,0.1) inset',
  py: 2.5,
  px: 3,
};

export default function Navigator(props) {
  const { ...other } = props;
  let isLogged = useSelector((state) => state.user.value.isLogged);
  const navigate = useNavigate();
  const [categories, setCategories] = React.useState([
    {
      id: 'Actions',
      children: [
        {
          id: 0,
          name: 'What are we voting for?',
          icon: <InfoRoundedIcon />,
          navigateTo: "/whatarewevotingfor"
        },
        { id: 1, name: 'Who are the candidates?', icon: <PermContactCalendarRoundedIcon />, navigateTo: "/whoarethecandidates" },
        { id: 2, name: 'How to vote?', icon: <QuestionMarkIcon />, navigateTo: "/howtovote"},
        { id: 3, name: 'Cast a vote', icon: <HowToVoteRoundedIcon />, navigateTo: "/vote"},
        { id: 4, name: 'Results', icon: <BarChartRoundedIcon />, navigateTo: "/results"}
      ,]
    }
  ])

  const [votingSession, setVotingSession] = React.useState()

  const {data, isLoading, isError, refetch} = useQuery(["votingSessionQuery"], () => {
      const token = `Bearer ${localStorage.getItem("jwt_token")}`
      if (localStorage.getItem("jwt_token") !== null) {
        return Axios
          .get(
              "http://localhost:8080/voting-session",
              { headers: {
                  "Authorization" : token
              }}
          )
          .then((response) => setVotingSession(response.data))
          .catch((error) => {
            if (error.response.status === 403) {
              alert("Session has expired. Please log in again!");
              navigate("/signin");
            }
        })
      }
      return null
  })

  const convertToReadableDate = (rawDate) => {
    let finalDate = "not available"
    if (rawDate !== undefined && isLogged) {
      finalDate = rawDate.substring(0, 10) + " " + rawDate.substring(11, 16)
    }
    return finalDate
  };

  const changeItemsActive = (id) => {
    const newItemsActive = [false, false, false, false, false];
    newItemsActive[id] = true;
    props.setItemsActive(newItemsActive);
  }

  const resetItemsActive = () => {
    const newItemsActive = [false, false, false, false, false];
    props.setItemsActive(newItemsActive);
  }

  return (
    <Drawer variant="permanent" {...other}>
      <List disablePadding>
        <ListItem sx={{ ...item, ...itemCategory, fontSize: 22, color: '#fff' }}>
          PoliVote
        </ListItem>

        <ListItem sx={{ ...item, ...itemCategory }}>
          <ListItemButton onClick={() => {resetItemsActive(); navigate("/")}}>
            <ListItemIcon>
              <HomeIcon/>
            </ListItemIcon>
            <ListItemText>Home Page</ListItemText>
          </ListItemButton>
        </ListItem>
        {categories.map(({ id, children }) => (
          <Box key={id} sx={{ bgcolor: '#101F33' }}>
            <ListItem sx={{ py: 2, px: 3 }}>
              <ListItemText sx={{ color: '#fff', fontSize: '120%' }} disableTypography>{id}</ListItemText>
            </ListItem>
            {children.map(({ id, name: childId, icon, navigateTo }) => (
              <ListItem disablePadding key={childId}>
                <ListItemButton selected={props.itemsActive[id]} sx={item} onClick={() => {changeItemsActive(id); navigate(navigateTo)}}>
                  <ListItemIcon>{icon}</ListItemIcon>
                  <ListItemText>{childId}</ListItemText>
                </ListItemButton>
              </ListItem>
            ))}
            <Divider sx={{ mt: 2 }} />
          </Box>
        ))}
        
        <Box sx={{ bgcolor: '#101F33' }}>
          <ListItem>
            <ListItemText sx={{ color: '#ccc', fontSize: '120%' }} disableTypography>When does it start?</ListItemText>
          </ListItem>
          <Chip label={convertToReadableDate(votingSession?.startingAt)} color="success" clickable/>
          <Divider sx={{ mt: 2}} />

          <ListItem>
            <ListItemText sx={{ color: '#ccc', fontSize: '120%' }} disableTypography>When does it end?</ListItemText>
          </ListItem>
          <Chip label={convertToReadableDate(votingSession?.endingAt)} color="error" size='medium' clickable/>
          <Divider sx={{ mt: 1 }} />
          
          <ListItem>
            <ListItemText sx={{ color: '#ccc', fontSize: '120%' }} disableTypography>Session status</ListItemText>
          </ListItem>
          <Chip color="error" label={votingSession && isLogged ? votingSession.votingSessionStatus : "not available"} clickable/>
        </Box>
      </List>
    </Drawer>
  );
}
