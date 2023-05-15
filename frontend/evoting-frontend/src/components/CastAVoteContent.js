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
import Election from './Election.json';
import { JSEncrypt } from "jsencrypt";
import CryptoJS from 'crypto-js';

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

	const castVote = async (userPublicKey, userPrivateKey, userChoice) => {
		console.log(signVote("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", "plm"))
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
				alert("You are not allowed to vote. Reason: " + reason);
			}
		}
	}

	const encryptVote = (message) => {
		const encrypt = new JSEncrypt();

		const publicKeyString = '-----BEGIN PUBLIC KEY-----\n' + props.votingSession.votingSessionPublicKey + '\n-----END PUBLIC KEY-----\n';
		encrypt.setPublicKey(publicKeyString);
		return encrypt.encrypt(message);
	}

	const signVote = (userPublicKey, userPrivateKey, encryptedVote) => {
		const stringToBeHashed = userPublicKey + encryptedVote;
		const sign = new JSEncrypt();
		const privateKey = '-----BEGIN RSA PRIVATE KEY-----\n' + userPrivateKey + '\n-----END RSA PRIVATE KEY-----\n';
		
		sign.setPrivateKey(privateKey);
		return sign.sign(stringToBeHashed, CryptoJS.SHA256, 'sha-256')
	}

	if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "IN_PROGRESS") {
  	return votingAvailableContent(candidateList, castVote)
	} else {
		// return voteUnavailableContent(isLogged)
		return votingAvailableContent(candidateList, castVote)
	}
}