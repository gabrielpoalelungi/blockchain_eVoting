import * as React from 'react';

import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Axios from "axios";
import { useNavigate } from "react-router-dom";
import PeopleIcon from '@mui/icons-material/People';

export default function AdminAddVotersToContractContent() {
  const navigate = useNavigate();

  const addVotersToContract = () => {
    const token = `Bearer ${localStorage.getItem("jwt_token")}`
    if (localStorage.getItem("jwt_token") !== null) {
      return Axios
        .post(
            "http://localhost:8080/voting-session/add-all-voters", null,
            { headers: {
                "Authorization" : token
            }}
        )
        .then((response) => {
          alert("Voters added successfully!");
          navigate("/admin")
        })
        .catch((error) => {
          if (error.response.status === 403) {
            console.log(error)
            alert("Session has expired. Please log in again!");
            navigate("/signin");
          } else {
            alert("Error " + error.response.status + ": " + error.response.data);
            navigate("/admin")
          }
      })
    }
  }

  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <PeopleIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Add Voters to Contract
          </Typography>
          <Box sx={{ mt: 3 }}>
            <Button
              fullWidth
              variant="contained"
              color="success"
              sx={{ mt: 3, mb: 2 }}
              onClick={addVotersToContract}
            >
              Add Voters to Contract
            </Button>
          </Box>
        </Box>
      </Container>
    </Paper>
  );
}
