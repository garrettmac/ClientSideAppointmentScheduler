# ClientSideAppointmentScheduler

This project operates within a multitheaded client-server enviorment. This is the client side android application
and will send and return appointment infromation once requested from server where the appointments are set. 

There are two server-side desktop applications this works in parrallel with. On is mulitheaded 
"ServerSideAppointmentScheduler_MultiThreaded" and the other is not  
"ServerSideAppointmentScheduler_Non-MultiThreaded"

The Clientside is not multitheaded. The updated message for the available appointment will not display until the
day selector is selected. The mulithead implementation on the client-side are returning Void in the Async

