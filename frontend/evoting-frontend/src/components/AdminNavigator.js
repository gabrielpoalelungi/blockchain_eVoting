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
import EditIcon from '@mui/icons-material/Edit';
import AddIcon from '@mui/icons-material/Add';
import PowerSettingsNewIcon from '@mui/icons-material/PowerSettingsNew';
import PeopleIcon from '@mui/icons-material/People';
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

export default function AdminNavigator(props) {
  const { ...other } = props;
  let isLogged = useSelector((state) => state.user.value.isLogged);
  let role = useSelector((state) => state.user.value.role);
  const navigate = useNavigate();
  const [categories, setCategories] = React.useState([
    {
      id: 'Actions',
      children: [
        {
          id: 0,
          name: 'Add Candidate',
          icon: <AddIcon />,
          navigateTo: "/admin/addcandidate"
        },
        { id: 1, name: 'Edit Candidates', icon: <EditIcon />, navigateTo: "/admin/editcandidates" },
        { id: 2, name: 'Create Voting Session', icon: <AddIcon />, navigateTo: "/admin/createvotesession" },
        { id: 3, name: 'Edit Voting Session', icon: <EditIcon />, navigateTo: "/admin/editvotesession"},
        { id: 4, name: 'Add All Voters to Smart Contract', icon: <PeopleIcon />, navigateTo: "/admin/addvoters"},
        { id: 5, name: 'Start/End Voting Session', icon: <PowerSettingsNewIcon />, navigateTo: "/admin/startendvotesession"}
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

  if (role !== 'ADMIN') {
    navigate("/")
    return;
  }

  return (
    <Drawer variant="permanent" {...other}>
      <List disablePadding>
        <ListItem sx={{ ...item, ...itemCategory, fontSize: 22, color: '#fff' }}>
          PoliVote
        </ListItem>

        <ListItem sx={{ ...item, ...itemCategory }}>
          <ListItemButton onClick={() => {resetItemsActive(); navigate("/admin")}}>
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
