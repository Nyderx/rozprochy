from hospital_pb2 import *
import hospital_pb2_grpc

def readRequirements():
    params = []

    while True:
        if input("Type y if u want to add requirement: ") is not "y":
            break

        paramName = input("Type in param name: ")
        min = int(input("Type in min value: "))
        max = int(input("Type in max value: "))
        params.append(RowQuery(name=paramName, min=min, max=max))

    return params


def readRecords(stub, patientId):
    records = stub.GetAll(DoctorQuery(patient = patientId, rowsRequirements=readRequirements()))

    for record in records:
        print(record)


def run():
    channel = grpc.insecure_channel('localhost:10000')
    stub =  hospital_pb2_grpc.DoctorServiceStub(channel)

    finished = False

    while not finished:
        read = input("Type r if u want to read patient records, type q to quit: ")
        patientId = input("Type in patient id: ")

        if read is "q":
            finished = True
        elif read is "r":
            readRecords(stub, patientId)


try:
    run()
except ValueError:
   print("Wrong type of value typed")
except grpc._channel._Rendezvous:
   print("Problem with connection")