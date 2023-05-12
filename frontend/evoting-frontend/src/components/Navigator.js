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

const categories = [
  {
    id: 'Actions',
    children: [
      {
        id: 'What are we voting for?',
        icon: <InfoRoundedIcon />
      },
      { id: 'Who are the candidates?', icon: <PermContactCalendarRoundedIcon /> },
      { id: 'How to vote?', icon: <QuestionMarkIcon /> },
      { id: 'Cast a vote', icon: <HowToVoteRoundedIcon /> },
      { id: 'Results', icon: <BarChartRoundedIcon /> }
    ,]
  }
];

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

  return (
    <Drawer variant="permanent" {...other}>
      <List disablePadding>
        <ListItem sx={{ ...item, ...itemCategory, fontSize: 22, color: '#fff' }}>
          PoliVote
        </ListItem>

        <ListItem sx={{ ...item, ...itemCategory }}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText>Home Page</ListItemText>
        </ListItem>

        {categories.map(({ id, children }) => (
          <Box key={id} sx={{ bgcolor: '#101F33' }}>
            <ListItem sx={{ py: 2, px: 3 }}>
              <ListItemText sx={{ color: '#fff', fontSize: '120%' }} disableTypography>{id}</ListItemText>
            </ListItem>
            {children.map(({ id: childId, icon }) => (
              <ListItem disablePadding key={childId}>
                <ListItemButton sx={item} onClick={() => alert("test")}>
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
          <Chip label="15.06.2023 09:00" color="success" clickable/>
          <Divider sx={{ mt: 2}} />

          <ListItem>
            <ListItemText sx={{ color: '#ccc', fontSize: '120%' }} disableTypography>When does it end?</ListItemText>
          </ListItem>
          <Chip label="15.06.2023 21:00" color="error" size='medium' clickable/>
          <Divider sx={{ mt: 1 }} />
          
          <ListItem>
            <ListItemText sx={{ color: '#ccc', fontSize: '120%' }} disableTypography>Session status</ListItemText>
          </ListItem>
          <Chip color="error" label="Not started" clickable/>
        </Box>
      </List>
    </Drawer>
  );
}
