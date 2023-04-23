const { assert } = require("chai")

const Election = artifacts.require('./Election.sol')

require('chai')
    .use(require('chai-as-promised'))
    .should()

contract('Election', ([deployer, seller, buyer]) => {
    let election;

    before(async() => {
        election = await Election.deployed()
    })

    beforeEach(async() => {
        election = await Election.new()
    })

    describe('deployment', async () => {
        it('deploys successfully', async () => {
            const address = await election.address
            assert.notEqual(address, 0x0)
        })

        it('has isVoteStarted and isVoteFinished on false', async () => {
            const isVoteStarted = await election.isVoteStarted();
            const isVoteFinished = await election.isVoteFinished();
            assert.equal(isVoteStarted, false);
            assert.equal(isVoteFinished, false);
        })
    })

    describe('chaning voting session state', async () => {
        it('starts Vote successfully', async () => {
            let isVoteStarted = await election.isVoteStarted();
            assert.equal(isVoteStarted, false);

            await election.startVote();
            isVoteStarted = await election.isVoteStarted();
            assert.equal(isVoteStarted, true);
        })

        it('ends Vote successfully', async () => {
            let isVoteFinished = await election.isVoteFinished();
            assert.equal(isVoteFinished, false);

            await election.endVote();
            isVoteFinished = await election.isVoteFinished();
            assert.equal(isVoteFinished, true);
        })
    })

    describe('adding voter to the list', async () => {

        it('adding a voter successfully', async () => {
            let isVoteStarted = await election.isVoteStarted();
            assert.equal(isVoteStarted, false);

            let dummyPublicKey = 'public-key'
            result = await election.addVoter(dummyPublicKey);

            const event = result.logs[0].args
            assert.equal(event.isRegistered, true);
            assert.equal(event.hasVoted, false);
        })

        it('adding the same voter should fail', async () => {
            let dummyPublicKey = 'public-key'
            await election.addVoter(dummyPublicKey);
            result = await election.addVoter(dummyPublicKey).should.be.rejected;
            assert.equal(result.reason, "Voter already registered")
        })

        it('adding a voter after startVote should fail', async () => {
            let dummyPublicKey = 'public-key'
            await election.startVote();

            result = await election.addVoter(dummyPublicKey).should.be.rejected;
            assert.equal(result.reason, "Voting session has started already")
        })
    })

    describe('casting votes', async () => {
        it('casting a vote successfully', async () => {
            let dummyPublicKey = 'public-key'
            result = await election.addVoter(dummyPublicKey);

            await election.startVote();
            let isVoteStarted = await election.isVoteStarted();
            assert.equal(isVoteStarted, true);

            let isVoteFinished = await election.isVoteFinished();
            assert.equal(isVoteFinished, false);

            result = await election.addVote(dummyPublicKey, "encryptedVote", "signature");
            const event = result.logs[0].args
            assert.equal(event.publicKey, dummyPublicKey)
            assert.equal(event.encryptedVote, "encryptedVote")
            assert.equal(event.signature, "signature")

        })

        it('casting a second vote by same voter should fail', async () => {
            let dummyPublicKey = 'public-key'
            result = await election.addVoter(dummyPublicKey);

            await election.startVote();

            await election.addVote(dummyPublicKey, "encryptedVote", "signature");
            result = await election.addVote(dummyPublicKey, "encryptedVote", "signature").should.be.rejected;
            assert.equal(result.reason, "Unauthorized voter")
        })

        it('casting a vote before startVote should fail', async () => {
            let dummyPublicKey = 'public-key'
            await election.addVoter(dummyPublicKey);

            result = await election.addVote(dummyPublicKey, "encryptedVote", "signature").should.be.rejected;
            assert.equal(result.reason, "Voting session not in progress")
        })

        it('casting a vote by a non-existing voter should fail', async () => {
            let dummyPublicKey = 'public-key'
            await election.startVote();

            result = await election.addVote(dummyPublicKey, "encryptedVote", "signature").should.be.rejected;
            assert.equal(result.reason, "Unauthorized voter")
        })
    })

    describe('retrieving votes', async () => {
        it('retrieving votes successfully', async () => {
            let dummyPublicKey = 'public-key'
            await election.addVoter(dummyPublicKey);
            await election.startVote();
            await election.addVote(dummyPublicKey, "encryptedVote", "signature");
            
            await election.endVote();
            result = election.getAllVotes();
            //TODO testing for both fail and success ones
        })
    })
})