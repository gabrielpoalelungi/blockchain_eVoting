import * as React from 'react';

import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import { useSelector} from "react-redux";
import { useQuery } from '@tanstack/react-query';
import Axios from "axios";
import { useNavigate } from "react-router-dom";
import { Box } from '@mui/system';
import Divider from '@mui/material/Divider';



export default function WhoAreTheCandidatesContent() {
  const [candidateList, setCandidateList] = React.useState([])
	const navigate = useNavigate();
	let isLogged = useSelector((state) => state.user.value.isLogged);

  useQuery(["getCandidatesQuery"], () => {
		const token = `Bearer ${localStorage.getItem("jwt_token")}`
		if (localStorage.getItem("jwt_token") !== null) {
			return Axios
				.get(
						"http://localhost:8080/candidates",
						{ headers: {
								"Authorization" : token
						}}
				)
				.then((response) => setCandidateList(response.data))
				.catch((error) => {
					if (error.response.status === 403) {
						alert("Session has expired. Please log in again!");
						navigate("/signin");
					}
			})
		}
		return null
	})

  return (
    <Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
      <Typography sx={{ my: 5, mx: 2 }} color="text.secondary" align="center" fontSize={"200%"}>
        Who are the candidates?
      </Typography>

      {candidateList.map((candidate) =>
        (
          <Box key={candidate.id} sx={{ bgcolor: '#CCCCCC' }}>
            <Typography sx={{ my: 0, mx: 2, textDecoration: 'underline' }} color="text.secondary" align="left" fontSize={"150%"} >
              {candidate.officialName}
            </Typography>
            <Typography sx={{ my: 0, mx: 2, }} color="text.secondary" align="left" fontSize={"110%"} >
              {candidate.description}
            </Typography>
            <Divider sx={{ mt: 2 }} />

          </Box>
        )
      )}
    </Paper>
  );
}
