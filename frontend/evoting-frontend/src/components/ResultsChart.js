import React, { useState, useEffect } from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import { Box } from '@mui/system';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { useSelector} from "react-redux";
import { useQuery } from '@tanstack/react-query';
import Axios from "axios";
import { useNavigate } from "react-router-dom";

ChartJS.register(ArcElement, Tooltip, Legend);

const theme = createTheme();

const resultsUnavailableContent = (isLogged) => {
	if (isLogged) {
		return (
			<ThemeProvider theme={theme}>
					<CssBaseline />
					<Typography sx={{ my: 5, mx: 2 }} color="red" align="center" fontSize={"150%"}>
							Please be advised that the voting session has not yet ended. We apologize for any inconvenience this may have caused you. We kindly suggest that you check back at a later time when the voting session is scheduled to end. Thank you for your understanding and patience.
					</Typography>
			</ThemeProvider>
		)
	} else {
		return (
			<ThemeProvider theme={theme}>
					<CssBaseline />
					<Typography sx={{ my: 5, mx: 2 }} color="red" align="center" fontSize={"150%"}>
						To access the details of the current voting session, please log in to your account.
					</Typography>
			</ThemeProvider>
		)
	}
}

const resultsAvailableContent = (resultsData) => {
	const options = {
		plugins: {
				legend: {
				labels: {
						font: {
						size: 26, // Adjust the font size of the labels
						},
				},
				},
		},
	};

	return (
		<Paper sx={{ maxWidth: "100%", height: "100%", margin: 'auto', overflow: 'hidden' }}>
			<Typography sx={{ my: 5, mx: 2 }} color="text.secondary" align="center" fontSize={"200%"}>
				Final results!
			</Typography>
			<Box
				sx={{
					display: 'flex',
					justifyContent: 'center',
					alignItems: 'center',
					height: '60vh', // Adjust the height of the chart container
				}}
			>
				<div style={{ width: '100%', height: '100%', marginLeft: '30%' }}>
					<Doughnut data={resultsData} options={options}/>
				</div>
			</Box>
		</Paper>
	)
}

export default function ResultsChart(props) {
	const [candidateList, setCandidateList] = React.useState([])
	const navigate = useNavigate();
	let isLogged = useSelector((state) => state.user.value.isLogged);

	useQuery(["getCandidatesQueryResultsChart"], () => {
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

	const data = {
		labels: candidateList.map(candidate => candidate.officialName),
		datasets: [
			{
				label: '# of Votes',
				data: candidateList.map(candidate => candidate.numberOfVotes),
				backgroundColor: [
					'rgba(255, 99, 132)',
					'rgba(54, 162, 235)',
					'rgba(255, 206, 86)',
					'rgba(75, 192, 192)',
					'rgba(153, 102, 255)',
					'rgba(255, 159, 64)',
				],
				borderColor: [
					'rgba(0, 0, 0, 1)',
					'rgba(0, 0, 0, 1)',
					'rgba(0, 0, 0, 1)',
					'rgba(0, 0, 0, 1)',
					'rgba(0, 0, 0, 1)',
					'rgba(0, 0, 0, 1)',
				],
				borderWidth: 1,
			},
		],
	};

	if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "FINISHED") {
		return resultsAvailableContent(data)
	} else {
		return resultsUnavailableContent(isLogged)
	}
}
