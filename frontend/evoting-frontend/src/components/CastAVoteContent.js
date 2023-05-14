import React, { useState, useEffect } from 'react';
import Web3 from 'web3';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useSelector} from "react-redux";
import { useQuery } from '@tanstack/react-query';
import Axios from "axios";
import { useNavigate } from "react-router-dom";
import Election from './Election.json'

const theme = createTheme();

const votingAvailableContent = (candidateList, castVote) => {
    return (
        <ThemeProvider theme={theme}>
					<CssBaseline />
					<Typography sx={{ my: 5, mx: 2 }} color="text.secondary" align="center" fontSize={"200%"}>
						Vote your favourite candidate!
					</Typography>
					<Typography sx={{ my: 5, mx: 2 }} color="red" align="center" fontSize={"150%"}>
						Kindly take note that once you have cast your vote, it is irrevocable and cannot be cancelled or changed. We kindly suggest that you carefully consider your decision before submitting your vote to ensure that it accurately reflects your choice.
					</Typography>
					<main>
						<Container sx={{ py: 10 }} maxWidth="md">
							<Grid container spacing={4}>
									{candidateList.map((candidate) => (
										<Grid item key={candidate.officialName} xs={12} sm={6} md={4}>
											<Card
											sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}
											>
											<CardMedia
													component="img"
													image="https://xsgames.co/randomusers/avatar.php?g=male"
													alt="random"
											/>
											<CardContent sx={{ flexGrow: 1 }}>
												<Typography gutterBottom variant="h5" component="h2">
												{candidate.officialName}
												</Typography>
											</CardContent>
											<CardActions>
												<Button size="small" variant="contained" color="success" onClick={castVote}>Vote</Button>
											</CardActions>
											</Card>
										</Grid>
									))}
							</Grid>
						</Container>
					</main>
        </ThemeProvider>
    )
}

const voteUnavailableContent = (isLogged) => {
	if (isLogged) {
		return (
			<ThemeProvider theme={theme}>
					<CssBaseline />
					<Typography sx={{ my: 5, mx: 2 }} color="red" align="center" fontSize={"150%"}>
							Please be advised that the voting session has not yet commenced. We apologize for any inconvenience this may have caused you. We kindly suggest that you check back at a later time when the voting session is scheduled to begin. Thank you for your understanding and patience.
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

export default function CastAVoteContent(props) {
	const [candidateList, setCandidateList] = React.useState([])
	const [electionSmartContract, setElectionSmartContract] = React.useState(null);
	const [loading, setLoading] = useState(true);
	const [addVoteFunction, setAddVoteFunction] = useState(null);
	const [account, setAccount] = useState('');

	let isLogged = useSelector((state) => state.user.value.isLogged);
	const navigate = useNavigate();

	useEffect(() => {
    const loadBlockchainData = async () => {
      const web3 = window.web3;
      const accounts = await web3.eth.getAccounts();
      setAccount(accounts[0]);

      const networkId = await web3.eth.net.getId();
      const networkData = Election.networks[networkId];

      if(networkData) {
        const election = new web3.eth.Contract(Election.abi, networkData.address);
        setElectionSmartContract(election);
				// console.log(await election.methods.addVote("plm", "plm", "plm").call())

        setLoading(false);
      } else {
        window.alert('Election contract not deployed to detected network.');
      }
    }

		const loadWeb3 = async () => {
      if (window.ethereum) {
        window.web3 = new Web3(window.ethereum);
        await window.ethereum.enable();
      }
      else if (window.web3) {
        window.web3 = new Web3(window.web3.currentProvider);
      }
      else {
        window.alert('Non-Ethereum browser detected. You should consider trying MetaMask!');
      }
    }

    loadWeb3();
    loadBlockchainData();
  }, []);

	const {data, isLoading, isError, refetch} = useQuery(["getCandidatesQuery"], () => {
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

	const castVote = async () => {
		if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "IN_PROGRESS") {
			try {
				const gasLimit = await electionSmartContract.methods.addVote("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuWKw72VdTutimMz25mI4LuEjkRjU26GCofYlrIKFDaEhC4Cl+DpbivQHDpr1GwWqMN+Pp4aHcEz73JgHzD0lB2tpGig2XcX/tzH5pOPtOFf0BZQjuZCTGmrMQeD9iJJJIcu0E20cWG96aYHFNDLZ6/PbTI8z72DWHizL43l9nkEL2bF68+wB2rhQM3KZYjH/OFphZ9I1wtymluMnWE0qzKuI+cAt/82xq8TPnNz9sMTaEWaOB/2nahqXY6yau+X4LqgCNSvrgvDAo7g+lpJBpnHtKJ7+jsi3Dy6mIHt1dC/ArjE+yxJ1jU4T6dTSgPIh+TSSG51R2rOakXNDKUQ8wIDAQAB", "plm", "plm").estimateGas({ from: account });

				const response = await electionSmartContract.methods.addVote("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuWKw72VdTutimMz25mI4LuEjkRjU26GCofYlrIKFDaEhC4Cl+DpbivQHDpr1GwWqMN+Pp4aHcEz73JgHzD0lB2tpGig2XcX/tzH5pOPtOFf0BZQjuZCTGmrMQeD9iJJJIcu0E20cWG96aYHFNDLZ6/PbTI8z72DWHizL43l9nkEL2bF68+wB2rhQM3KZYjH/OFphZ9I1wtymluMnWE0qzKuI+cAt/82xq8TPnNz9sMTaEWaOB/2nahqXY6yau+X4LqgCNSvrgvDAo7g+lpJBpnHtKJ7+jsi3Dy6mIHt1dC/ArjE+yxJ1jU4T6dTSgPIh+TSSG51R2rOakXNDKUQ8wIDAQAB", "plm", "plm").send({
					from: account,
					gas: gasLimit
				});
				alert("Vote casted");
			} catch (error) {
				const reasonMatch = error.message.match(/"reason": "(.*)",/);
				const reason = reasonMatch ? reasonMatch[1] : "Unknown reason";
				console.log(reason);
				alert(reason);
			}
		}
	}

	if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "IN_PROGRESS") {
  	return votingAvailableContent(candidateList, castVote)
	} else {
		return voteUnavailableContent(isLogged)
	}
}