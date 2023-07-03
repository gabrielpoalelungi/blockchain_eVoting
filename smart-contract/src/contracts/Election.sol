// SPDX-License-Identifier: UNLICENSED
pragma solidity 0.8.19;

contract Election {
    // The vote struct that represents the valuable payload of a final vote
    struct Vote {
        string publicKey;
        string encryptedVote;
        string signature;
    }

    // Voter struct used to keep status of each one of them;
    struct Voter {
        bool isRegistered;
        bool hasVoted;
    }

    // Here are stored all the votes
    uint private voteCount = 0;
    mapping(uint => Vote) private votes;

    // Here are stored all the registered voters.
    mapping(bytes32 => Voter) private voters;

    // Status of the current voting session
    bool public isVoteStarted;
    bool public isVoteFinished;

    // The admin address: the first address from Ganache workspace
    // To test modifiers, change this to anything you want (ex: 0xcafebabef00ddeadbeefcafebabef00ddeadbeef)
    address private authorizedAddress = 0x17987d50dD4439Ed4Cf3975Ef6752D1C7a076cA3;

    // Modifiers. The names suggest what they do
    modifier onlyAuthorized() {
        require(msg.sender == authorizedAddress, "Unauthorized caller");
        _;
    }

    modifier onlyAfterVoteFinished() {
        require(isVoteFinished == true, "Voting session not finished yet");
        _;
    }

    modifier onlyAfterVoteStartedAndNotFinished() {
        require(isVoteFinished == false && isVoteStarted == true, "Voting session not in progress");
        _;
    }

    modifier onlyBeforeVoteStarted() {
        require(isVoteFinished == false && isVoteStarted == false, "Voting session has started already");
        _;
    }

    // Creating the voting session
    constructor() {
        isVoteFinished = false;
        isVoteStarted = false;
    }

    // The events are used for logging
    event VoteStartedOrFinished (
        string message
    );

    event VoteCasted (
        string publicKey,
        string message
    );

    event VoterAdded (
        bytes32 hashedPublicKey,
        bool isRegistered,
        bool hasVoted
    );

    // One-way modifying function in order to start the voting session 
    function startVote() public onlyAuthorized {
        isVoteStarted = true;
        emit VoteStartedOrFinished("Vote has started");
    }

    // One-way modifying function in order to end the voting session 
    function endVote() public onlyAuthorized {
        isVoteFinished = true;
        emit VoteStartedOrFinished("Vote has finished");
    }

    // Add a voter to the voter list by hashing his/her public key.
    function addVoter(string memory _publicKey) public onlyAuthorized onlyBeforeVoteStarted {
        bytes32 hashedPublicKey = keccak256(abi.encodePacked(_publicKey));
        Voter memory currentVoter = getVoter(hashedPublicKey);
        require(currentVoter.isRegistered == false, "Voter already registered");

        voters[hashedPublicKey] = Voter(true, false);
        emit VoterAdded(hashedPublicKey, true, false);
    }
    //lala

    // Get details of a voter, be him/her registered or not
    function getVoter(bytes32 hashedPublicKey) private view returns (Voter memory){
        return voters[hashedPublicKey];
    }

    // Add a vote to the votes mapping
    function addVote(
        string memory _publicKey, 
        string memory _encryptedVote, 
        string memory _signature) public onlyAfterVoteStartedAndNotFinished 
    {
        bytes32 hashedPublicKey = keccak256(abi.encodePacked(_publicKey));
        Voter memory currentVoter = getVoter(hashedPublicKey);
        require(currentVoter.hasVoted == false && currentVoter.isRegistered == true, "Unauthorized voter");

        voteCount++;
        votes[voteCount] = Vote(_publicKey, _encryptedVote, _signature);
        voters[hashedPublicKey].hasVoted = true;
        emit VoteCasted(_publicKey, "Vote has been registered for the voter");
    }

    // Return all the votes casted in this session
    function getAllVotes() public view onlyAfterVoteFinished returns (Vote[] memory) {
        Vote[] memory returnedVotes = new Vote[](voteCount);
        for (uint i = 0; i < voteCount; i++) {
            returnedVotes[i] = votes[i+1];
        }
        return returnedVotes;
    }
}