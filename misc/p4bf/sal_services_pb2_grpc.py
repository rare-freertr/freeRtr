# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
import grpc

import sal_services_pb2 as sal__services__pb2


class SwitchServiceStub(object):
  # missing associated documentation comment in .proto file
  pass

  def __init__(self, channel):
    """Constructor.

    Args:
      channel: A grpc.Channel.
    """
    self.TestConnection = channel.unary_unary(
        '/sal_services.SwitchService/TestConnection',
        request_serializer=sal__services__pb2.NoInput.SerializeToString,
        response_deserializer=sal__services__pb2.ServerProp.FromString,
        )
    self.GetSwitchModel = channel.unary_unary(
        '/sal_services.SwitchService/GetSwitchModel',
        request_serializer=sal__services__pb2.NoInput.SerializeToString,
        response_deserializer=sal__services__pb2.SwitchModel.FromString,
        )
    self.GetPortConfig = channel.unary_unary(
        '/sal_services.SwitchService/GetPortConfig',
        request_serializer=sal__services__pb2.PortId.SerializeToString,
        response_deserializer=sal__services__pb2.PortConfig.FromString,
        )
    self.StartTofino = channel.unary_unary(
        '/sal_services.SwitchService/StartTofino',
        request_serializer=sal__services__pb2.NoInput.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.StartGearBox = channel.unary_unary(
        '/sal_services.SwitchService/StartGearBox',
        request_serializer=sal__services__pb2.NoInput.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.StartPTP = channel.unary_unary(
        '/sal_services.SwitchService/StartPTP',
        request_serializer=sal__services__pb2.NoInput.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.AddPort = channel.unary_unary(
        '/sal_services.SwitchService/AddPort',
        request_serializer=sal__services__pb2.PortInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.DelPort = channel.unary_unary(
        '/sal_services.SwitchService/DelPort',
        request_serializer=sal__services__pb2.PortId.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.SetSpeed = channel.unary_unary(
        '/sal_services.SwitchService/SetSpeed',
        request_serializer=sal__services__pb2.SpeedInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.SetFec = channel.unary_unary(
        '/sal_services.SwitchService/SetFec',
        request_serializer=sal__services__pb2.FecInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.SetFc = channel.unary_unary(
        '/sal_services.SwitchService/SetFc',
        request_serializer=sal__services__pb2.FCInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.SetAN = channel.unary_unary(
        '/sal_services.SwitchService/SetAN',
        request_serializer=sal__services__pb2.ANInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.EnablePort = channel.unary_unary(
        '/sal_services.SwitchService/EnablePort',
        request_serializer=sal__services__pb2.EnableInfo.SerializeToString,
        response_deserializer=sal__services__pb2.Response.FromString,
        )
    self.GetSFPCInfo = channel.unary_unary(
        '/sal_services.SwitchService/GetSFPCInfo',
        request_serializer=sal__services__pb2.PortId.SerializeToString,
        response_deserializer=sal__services__pb2.SFPInfo.FromString,
        )


class SwitchServiceServicer(object):
  # missing associated documentation comment in .proto file
  pass

  def TestConnection(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def GetSwitchModel(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def GetPortConfig(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def StartTofino(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def StartGearBox(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def StartPTP(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def AddPort(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def DelPort(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def SetSpeed(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def SetFec(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def SetFc(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def SetAN(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def EnablePort(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def GetSFPCInfo(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')


def add_SwitchServiceServicer_to_server(servicer, server):
  rpc_method_handlers = {
      'TestConnection': grpc.unary_unary_rpc_method_handler(
          servicer.TestConnection,
          request_deserializer=sal__services__pb2.NoInput.FromString,
          response_serializer=sal__services__pb2.ServerProp.SerializeToString,
      ),
      'GetSwitchModel': grpc.unary_unary_rpc_method_handler(
          servicer.GetSwitchModel,
          request_deserializer=sal__services__pb2.NoInput.FromString,
          response_serializer=sal__services__pb2.SwitchModel.SerializeToString,
      ),
      'GetPortConfig': grpc.unary_unary_rpc_method_handler(
          servicer.GetPortConfig,
          request_deserializer=sal__services__pb2.PortId.FromString,
          response_serializer=sal__services__pb2.PortConfig.SerializeToString,
      ),
      'StartTofino': grpc.unary_unary_rpc_method_handler(
          servicer.StartTofino,
          request_deserializer=sal__services__pb2.NoInput.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'StartGearBox': grpc.unary_unary_rpc_method_handler(
          servicer.StartGearBox,
          request_deserializer=sal__services__pb2.NoInput.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'StartPTP': grpc.unary_unary_rpc_method_handler(
          servicer.StartPTP,
          request_deserializer=sal__services__pb2.NoInput.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'AddPort': grpc.unary_unary_rpc_method_handler(
          servicer.AddPort,
          request_deserializer=sal__services__pb2.PortInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'DelPort': grpc.unary_unary_rpc_method_handler(
          servicer.DelPort,
          request_deserializer=sal__services__pb2.PortId.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'SetSpeed': grpc.unary_unary_rpc_method_handler(
          servicer.SetSpeed,
          request_deserializer=sal__services__pb2.SpeedInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'SetFec': grpc.unary_unary_rpc_method_handler(
          servicer.SetFec,
          request_deserializer=sal__services__pb2.FecInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'SetFc': grpc.unary_unary_rpc_method_handler(
          servicer.SetFc,
          request_deserializer=sal__services__pb2.FCInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'SetAN': grpc.unary_unary_rpc_method_handler(
          servicer.SetAN,
          request_deserializer=sal__services__pb2.ANInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'EnablePort': grpc.unary_unary_rpc_method_handler(
          servicer.EnablePort,
          request_deserializer=sal__services__pb2.EnableInfo.FromString,
          response_serializer=sal__services__pb2.Response.SerializeToString,
      ),
      'GetSFPCInfo': grpc.unary_unary_rpc_method_handler(
          servicer.GetSFPCInfo,
          request_deserializer=sal__services__pb2.PortId.FromString,
          response_serializer=sal__services__pb2.SFPInfo.SerializeToString,
      ),
  }
  generic_handler = grpc.method_handlers_generic_handler(
      'sal_services.SwitchService', rpc_method_handlers)
  server.add_generic_rpc_handlers((generic_handler,))
