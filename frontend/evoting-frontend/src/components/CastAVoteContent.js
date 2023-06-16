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
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';

const theme = createTheme();

const getRandomImage = () => {
    // Generate a random number between 1 and 1000
    const randomId = Math.floor(Math.random() * 75) + 1;
    return `https://xsgames.co/randomusers/assets/avatars/male/${randomId}.jpg`;
};

const votingAvailableContent = (candidateList, castVote, modalOpen, modalContent, handleModalClose) => {
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
											image={getRandomImage()}
											alt={candidate.officialName}
									/>
									<CardContent sx={{ flexGrow: 1 }}>
										<Typography gutterBottom variant="h5" component="h2">
											{candidate.officialName}
										</Typography>
									</CardContent>
									<CardActions>
										<Button size="small" variant="contained" color="success" onClick={() => castVote(candidate.officialName)}>Vote</Button>
									</CardActions>
									</Card>
								</Grid>
							))}
					</Grid>
				</Container>
			</main>
			<Modal
				open={modalOpen}
				onClose={handleModalClose}
				closeAfterTransition
				aria-labelledby="parent-modal-title"
  				aria-describedby="parent-modal-description"
				>
				<Box sx={{
					display: 'flex',
					alignItems: 'center',
					justifyContent: 'center',
					width: '100%',
					height: '100%',
					}}>
					<Card>
						<CardContent>
						<Typography variant="h6" gutterBottom align="center">
							{modalContent}
						</Typography>
						</CardContent>
						<CardActions sx={{ justifyContent: 'center' }}>
						<Button onClick={handleModalClose} variant="contained" color="primary">
							Close
						</Button>
						</CardActions>
					</Card>
				</Box>
			</Modal>
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
	const [account, setAccount] = useState('');
	const [userInfo, setUserInfo] = React.useState();
	const [modalOpen, setModalOpen] = useState(false);
	const [modalContent, setModalContent] = useState("");

	let isLogged = useSelector((state) => state.user.value.isLogged);
	const navigate = useNavigate();

	const handleModalClose = () => {
		setModalOpen(false);
		setModalContent("");
	  };

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

	const {data, isLoading, isError, refetch} = useQuery(["getUserDetailsForVoting"], () => {
		const token = `Bearer ${localStorage.getItem("jwt_token")}`
		if (localStorage.getItem("jwt_token") !== null) {
		  return Axios
			.get(
				"http://localhost:8080/voters/logged-user",
				{ headers: {
					"Authorization" : token
				}}
			)
			.then((response) => setUserInfo(response.data))
			.catch((error) => {
			  console.log(error)
			  if (error.response.status === 403) {
				alert("Session has expired. Please log in again!");
				navigate("/signin");
			  }
		  })
		}
		return null
	})

	const castVote = async (userChoice) => {
		if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "IN_PROGRESS") {
			const encryptedVote = encryptVote(userChoice);
			const signature = signVote(userInfo.publicKey, userInfo.privateKey, encryptedVote);
			try {
				const gasLimit = await electionSmartContract.methods.addVote(userInfo.publicKey, encryptedVote, signature).estimateGas({ from: account });

				const response = await electionSmartContract.methods.addVote(userInfo.publicKey, encryptedVote, signature).send({
					from: account,
					gas: gasLimit
				});
				console.log("RESPONSE: ", response)

				setModalOpen(true);
				setModalContent("Vote casted");
			} catch (error) {
				const reasonMatch = error.message.match(/"reason": "(.*)",/);
				const reason = reasonMatch ? reasonMatch[1] : "Unknown reason";
				setModalOpen(true);
				setModalContent(reason + " or you already voted!");
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

		const signature = sign.sign(stringToBeHashed, CryptoJS.SHA256, 'sha-256')

		return signature
	}

	if (isLogged && props.votingSession && props.votingSession.votingSessionStatus === "IN_PROGRESS") {
  		return votingAvailableContent(candidateList, castVote, modalOpen, modalContent, handleModalClose)
	} else {
		return voteUnavailableContent(isLogged)
	}
}