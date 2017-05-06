from hospital_pb2 import *
import hospital_pb2_grpc


def readRecords(stub, patientId):
    records = stub.GetAll(PatientQuery(patient = patientId))

    for record in records:
        print(record)


def run():
    channel = grpc.insecure_channel('localhost:10000')
    stub =  hospital_pb2_grpc.PatientServiceStub(channel)

    patientId = input("Type in patient id: ")

    finished = False

    while not finished:
        read = input("Type r if u want to read your records, type q to quit: ")
        if read is "q":
            finished = True
        elif read is "r":
            readRecords(stub, patientId)


try:
    run()
except grpc._channel._Rendezvous:
    print("Problem with connection")