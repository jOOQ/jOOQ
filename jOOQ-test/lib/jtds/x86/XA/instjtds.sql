/*
 *	INSTJTDS.SQL
 *	Installs the XA stored procedure used by the JTDS driver 
 * 	Probably a good idea to backup the master database before 
 *      running this script
 */

use master
go

dump tran master with no_log
go

sp_dropextendedproc 'xp_jtdsxa' 
go

dump tran master with no_log
go

sp_addextendedproc 'xp_jtdsxa', 'jtdsXA.dll'
go

grant execute on xp_jtdsxa to public
go

dump tran master with no_log
go

checkpoint
go

